package com.conagra.mvp.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.IExtensionClickListener;
import io.rong.imkit.InputMenu;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.manager.AudioPlayManager;
import io.rong.imkit.manager.AudioRecordManager;
import io.rong.imkit.manager.InternalModuleManager;
import io.rong.imkit.manager.SendImageManager;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imkit.model.ConversationInfo;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.location.AMapRealTimeActivity;
import io.rong.imkit.plugin.location.IUserInfoProvider;
import io.rong.imkit.plugin.location.LocationManager;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imkit.widget.AlterDialogFragment;
import io.rong.imkit.widget.AutoRefreshListView;
import io.rong.imkit.widget.SingleChoiceDialog;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.CustomServiceConfig;
import io.rong.imlib.ICustomServiceListener;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.IRongCallback.ISendMediaMessageCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.location.RealTimeLocationConstant;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.CSGroupItem;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.CustomServiceMode;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.Message.ReceivedStatus;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.model.PublicServiceMenu;
import io.rong.imlib.model.PublicServiceMenuItem;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.ReadReceiptInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.PublicServiceCommandMessage;
import io.rong.message.ReadReceiptMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.push.RongPushClient;

public class ConversationFragment1 extends UriFragment implements AbsListView.OnScrollListener, IExtensionClickListener, IUserInfoProvider {
    private static final String TAG = "ConversationFragment1";
    private PublicServiceProfile mPublicServiceProfile;
    private View mRealTimeBar;
    private TextView mRealTimeText;
    private RongExtension mRongExtension;
    private boolean mEnableMention;
    private float mLastTouchY;
    private boolean mUpDirection;
    private float mOffsetLimit;
    private CSCustomServiceInfo mCustomUserInfo;
    private ConversationInfo mCurrentConversationInfo;
    private String mDraft;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    public static final int SCROLL_MODE_NORMAL = 1;
    public static final int SCROLL_MODE_TOP = 2;
    public static final int SCROLL_MODE_BOTTOM = 3;
    private static final int DEFAULT_HISTORY_MESSAGE_COUNT = 30;
    private static final int DEFAULT_REMOTE_MESSAGE_COUNT = 10;
    private static final int TIP_DEFAULT_MESSAGE_COUNT = 2;
    private String mTargetId;
    private ConversationType mConversationType;
    private boolean mReadRec;
    private boolean mSyncReadStatus;
    private int mNewMessageCount;
    private AutoRefreshListView mList;
    private Button mUnreadBtn;
    private ImageButton mNewMessageBtn;
    private TextView mNewMessageTextView;
    private MessageListAdapter mListAdapter;
    private View mMsgListView;
    private boolean mHasMoreLocalMessages;
    private int mLastMentionMsgId;
    private long mSyncReadStatusMsgTime;
    private boolean mCSneedToQuit = true;
    private boolean robotType = true;
    private int source = 0;
    private boolean resolved = true;
    private boolean committing = false;
    private long enterTime;
    private int page;
    private boolean evaluate = true;
    ICustomServiceListener customServiceListener = new ICustomServiceListener() {
        public void onSuccess(CustomServiceConfig config) {
            if(config.isBlack) {
                ConversationFragment1.this.onCustomServiceWarning(ConversationFragment1.this.getString(io.rong.imkit.R.string.rc_blacklist_prompt), false);
            }

            if(config.robotSessionNoEva) {
                ConversationFragment1.this.evaluate = false;
                ConversationFragment1.this.mListAdapter.setEvaluateForRobot(true);
            }

        }

        public void onError(int code, String msg) {
            ConversationFragment1.this.onCustomServiceWarning(msg, false);
        }

        public void onModeChanged(CustomServiceMode mode) {
            ConversationFragment1.this.mRongExtension.setExtensionBarMode(mode);
            if(!mode.equals(CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN) && !mode.equals(CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN_FIRST)) {
                if(mode.equals(CustomServiceMode.CUSTOM_SERVICE_MODE_NO_SERVICE)) {
                    ConversationFragment1.this.evaluate = false;
                }
            } else {
                ConversationFragment1.this.robotType = false;
                ConversationFragment1.this.evaluate = true;
            }

        }

        public void onQuit(String msg) {
            if(!ConversationFragment1.this.committing) {
                ConversationFragment1.this.onCustomServiceWarning(msg, true);
            }

        }

        public void onPullEvaluation(String dialogId) {
            if(!ConversationFragment1.this.committing) {
                ConversationFragment1.this.onCustomServiceEvaluation(true, dialogId, ConversationFragment1.this.robotType, ConversationFragment1.this.evaluate);
            }

        }

        public void onSelectGroup(List<CSGroupItem> groups) {
            ConversationFragment1.this.onSelectCustomerServiceGroup(groups);
        }
    };

    public ConversationFragment1() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InternalModuleManager.getInstance().onLoaded();

        try {
            this.mEnableMention = RongContext.getInstance().getResources().getBoolean(io.rong.imkit.R.bool.rc_enable_mentioned_message);
        } catch (Resources.NotFoundException var5) {
            RLog.e("ConversationFragment1", "rc_enable_mentioned_message not found in rc_config.xml");
        }

        try {
            this.mReadRec = this.getResources().getBoolean(io.rong.imkit.R.bool.rc_read_receipt);
            this.mSyncReadStatus = this.getResources().getBoolean(io.rong.imkit.R.bool.rc_enable_sync_read_status);
        } catch (Resources.NotFoundException var4) {
            RLog.e("ConversationFragment1", "rc_read_receipt not found in rc_config.xml");
            var4.printStackTrace();
        }

        try {
            this.mCSneedToQuit = RongContext.getInstance().getResources().getBoolean(io.rong.imkit.R.bool.rc_stop_custom_service_when_quit);
        } catch (Resources.NotFoundException var3) {
            var3.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(io.rong.imkit.R.layout.rc_fr_conversation, container, false);
        this.mRongExtension = (RongExtension)view.findViewById(io.rong.imkit.R.id.rc_extension);
        this.mRongExtension.setExtensionClickListener(this);
        this.mRongExtension.setFragment(this);
        this.mOffsetLimit = 70.0F * this.getActivity().getResources().getDisplayMetrics().density;
        this.mMsgListView = this.findViewById(view, io.rong.imkit.R.id.rc_layout_msg_list);
        this.mList = (AutoRefreshListView)this.findViewById(this.mMsgListView, io.rong.imkit.R.id.rc_list);
        this.mList.requestDisallowInterceptTouchEvent(true);
        this.mList.setMode(AutoRefreshListView.Mode.START);
        this.mList.setTranscriptMode(2);
        this.mListAdapter = new MessageListAdapter(this.getActivity());
        this.mList.setAdapter(this.mListAdapter);
        this.mList.setOnRefreshListener(new AutoRefreshListView.OnRefreshListener() {
            public void onRefreshFromStart() {
                if(ConversationFragment1.this.mHasMoreLocalMessages) {
                    ConversationFragment1.this.getHistoryMessage(ConversationFragment1.this.mConversationType, ConversationFragment1.this.mTargetId, 30, 1);
                } else {
                    ConversationFragment1.this.getRemoteHistoryMessages(ConversationFragment1.this.mConversationType, ConversationFragment1.this.mTargetId, 10);
                }
            }

            public void onRefreshFromEnd() {
            }
        });
        this.mList.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == 2 && ConversationFragment1.this.mList.getCount() - ConversationFragment1.this.mList.getHeaderViewsCount() == 0) {
                    if(ConversationFragment1.this.mHasMoreLocalMessages) {
                        ConversationFragment1.this.getHistoryMessage(ConversationFragment1.this.mConversationType, ConversationFragment1.this.mTargetId, 30, 1);
                    } else if(ConversationFragment1.this.mList.getRefreshState() != AutoRefreshListView.State.REFRESHING) {
                        ConversationFragment1.this.getRemoteHistoryMessages(ConversationFragment1.this.mConversationType, ConversationFragment1.this.mTargetId, 10);
                    }

                    return true;
                } else {
                    if(event.getAction() == 1 && ConversationFragment1.this.mRongExtension.isExtensionExpanded()) {
                        ConversationFragment1.this.mRongExtension.collapseExtension();
                    }

                    return false;
                }
            }
        });
        if(RongContext.getInstance().getNewMessageState()) {
            this.mNewMessageTextView = (TextView)this.findViewById(view, io.rong.imkit.R.id.rc_new_message_number);
            this.mNewMessageBtn = (ImageButton)this.findViewById(view, io.rong.imkit.R.id.rc_new_message_count);
            this.mNewMessageBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ConversationFragment1.this.mList.smoothScrollToPosition(ConversationFragment1.this.mList.getCount() + 1);
                    ConversationFragment1.this.mNewMessageBtn.setVisibility(View.GONE);
                    ConversationFragment1.this.mNewMessageTextView.setVisibility(View.GONE);
                    ConversationFragment1.this.mNewMessageCount = 0;
                }
            });
        }

        if(RongContext.getInstance().getUnreadMessageState()) {
            this.mUnreadBtn = (Button)this.findViewById(this.mMsgListView, io.rong.imkit.R.id.rc_unread_message_count);
        }

        this.mList.addOnScrollListener(this);
        this.mListAdapter.setOnItemHandlerListener(new MessageListAdapter.OnItemHandlerListener() {
            public boolean onWarningViewClick(int position, final Message data, View v) {
                if(!ConversationFragment1.this.onResendItemClick(data)) {
                    RongIM.getInstance().deleteMessages(new int[]{data.getMessageId()}, new ResultCallback<Boolean>() {
                        public void onSuccess(Boolean aBoolean) {
                            if(aBoolean.booleanValue()) {
                                data.setMessageId(0);
                                setMessageData(data);
                                if(data.getContent() instanceof ImageMessage) {
                                    RongIM.getInstance().sendImageMessage(data, "", "", new RongIMClient.SendImageMessageCallback() {
                                        public void onAttached(Message message) {
                                        }

                                        public void onError(Message message, ErrorCode code) {
                                        }

                                        public void onSuccess(Message message) {
                                        }

                                        public void onProgress(Message message, int progress) {
                                        }
                                    });
                                } else if(data.getContent() instanceof LocationMessage) {
                                    RongIM.getInstance().sendLocationMessage(data, (String)null, (String)null, (IRongCallback.ISendMessageCallback)null);
                                } else if(data.getContent() instanceof FileMessage) {
                                    RongIM.getInstance().sendMediaMessage(data, (String)null, (String)null, (ISendMediaMessageCallback)null);
                                } else {
                                    RongIM.getInstance().sendMessage(data, (String)null, (String)null, new IRongCallback.ISendMessageCallback() {
                                        public void onAttached(Message message) {

                                        }

                                        public void onSuccess(Message message) {
                                        }

                                        public void onError(Message message, ErrorCode errorCode) {
                                        }
                                    });
                                }
                            }

                        }

                        public void onError(ErrorCode e) {
                        }
                    });
                }

                return true;
            }

            public void onReadReceiptStateClick(Message message) {
                ConversationFragment1.this.onReadReceiptStateClick(message);
            }
        });
        return view;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == 1) {
            this.mRongExtension.collapseExtension();
        } else if(scrollState == 0) {
            int last = this.mList.getLastVisiblePosition();
            if(this.mList.getCount() - last > 2) {
                this.mList.setTranscriptMode(1);
            } else {
                this.mList.setTranscriptMode(2);
            }

            if(this.mNewMessageBtn != null && last == this.mList.getCount() - 1) {
                this.mNewMessageCount = 0;
                this.mNewMessageBtn.setVisibility(View.GONE);
                this.mNewMessageTextView.setVisibility(View.GONE);
            }
        }

    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onResume() {
        RongPushClient.clearAllPushNotifications(this.getActivity());
        super.onResume();
    }

    public void getUserInfo(String userId, UserInfoCallback callback) {
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(userId);
        if(userInfo != null) {
            callback.onGotUserInfo(userInfo);
        }

    }

    protected void initFragment(Uri uri) {
        RLog.d("ConversationFragment1", "initFragment : " + uri + ",this=" + this);
        if(uri != null) {
            String typeStr = uri.getLastPathSegment().toUpperCase();
            this.mConversationType = ConversationType.valueOf(typeStr);
            this.mTargetId = uri.getQueryParameter("targetId");
            RongIMClient.getInstance().getTextMessageDraft(this.mConversationType, this.mTargetId, new ResultCallback<String>() {
                public void onSuccess(String s) {
                    ConversationFragment1.this.mDraft = s;
                    if(ConversationFragment1.this.mRongExtension != null) {
                        EditText editText = ConversationFragment1.this.mRongExtension.getInputEditText();
                        editText.setText(s);
                        editText.setSelection(editText.length());
                    }
                }
                public void onError(ErrorCode e) {
                }
            });
            this.mCurrentConversationInfo = ConversationInfo.obtain(this.mConversationType, this.mTargetId);
            RongContext.getInstance().registerConversationInfo(this.mCurrentConversationInfo);
            this.mRealTimeBar = this.mMsgListView.findViewById(io.rong.imkit.R.id.real_time_location_bar);
            this.mRealTimeText = (TextView)this.mMsgListView.findViewById(io.rong.imkit.R.id.real_time_location_text);
            if(this.mConversationType.equals(ConversationType.CUSTOMER_SERVICE) && this.getActivity() != null && this.getActivity().getIntent() != null && this.getActivity().getIntent().getData() != null) {
                this.mCustomUserInfo = (CSCustomServiceInfo)this.getActivity().getIntent().getParcelableExtra("customServiceInfo");
            }

            LocationManager.getInstance().bindConversation(this.getActivity(), this.mConversationType, this.mTargetId);
            LocationManager.getInstance().setUserInfoProvider(this);
//            LocationManager.getInstance().setParticipantChangedListener(new IParticipantChangedListener() {
//                public void onParticipantChanged(List<String> userIdList) {
//                    if(!ConversationFragment1.this.isDetached()) {
//                        if(userIdList != null) {
//                            if(userIdList.size() == 0) {
//                                ConversationFragment1.this.mRealTimeBar.setVisibility(View.VISIBLE);
//                            } else if(userIdList.size() == 1 && userIdList.contains(RongIM.getInstance().getCurrentUserId())) {
//                                ConversationFragment1.this.mRealTimeText.setText(ConversationFragment1.this.getResources().getString(io.rong.imkit.R.string.you_are_sharing_location));
//                                ConversationFragment1.this.mRealTimeBar.setVisibility(View.VISIBLE);
//                            } else if(userIdList.size() == 1 && !userIdList.contains(RongIM.getInstance().getCurrentUserId())) {
//                                ConversationFragment1.this.mRealTimeText.setText(String.format(ConversationFragment1.this.getResources().getString(io.rong.imkit.R.string.other_is_sharing_location), new Object[]{ConversationFragment1.this.getNameFromCache((String)userIdList.get(0))}));
//                                ConversationFragment1.this.mRealTimeBar.setVisibility(View.VISIBLE);
//                            } else {
//                                ConversationFragment1.this.mRealTimeText.setText(String.format(ConversationFragment1.this.getResources().getString(io.rong.imkit.R.string.others_are_sharing_location), new Object[]{Integer.valueOf(userIdList.size())}));
//                                ConversationFragment1.this.mRealTimeBar.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                    }
//                }
//            });
            this.mRongExtension.setConversation(this.mConversationType, this.mTargetId);
            this.mRealTimeBar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient.getInstance().getRealTimeLocationCurrentState(ConversationFragment1.this.mConversationType, ConversationFragment1.this.mTargetId);
                    if(status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {
                        final AlterDialogFragment intent = AlterDialogFragment.newInstance("", "加入位置共享", "取消", "加入");
                        intent.setOnAlterDialogBtnListener(new AlterDialogFragment.AlterDialogBtnListener() {
                            public void onDialogPositiveClick(AlterDialogFragment dialog) {
                                LocationManager.getInstance().joinLocationSharing();
                                Intent intentx = new Intent(ConversationFragment1.this.getActivity(), AMapRealTimeActivity.class);
                                ConversationFragment1.this.startActivity(intentx);
                            }

                            public void onDialogNegativeClick(AlterDialogFragment dialog) {
                                intent.dismiss();
                            }
                        });
                        intent.show(ConversationFragment1.this.getFragmentManager());
                    } else {
                        Intent intent1 = new Intent(ConversationFragment1.this.getActivity(), AMapRealTimeActivity.class);
                        ConversationFragment1.this.startActivity(intent1);
                    }

                }
            });
            if(!this.mConversationType.equals(ConversationType.CHATROOM)) {
                if(this.mConversationType != ConversationType.APP_PUBLIC_SERVICE && this.mConversationType != ConversationType.PUBLIC_SERVICE) {
                    if(this.mConversationType.equals(ConversationType.CUSTOMER_SERVICE)) {
                        this.enterTime = System.currentTimeMillis();
                        RongIMClient.getInstance().startCustomService(this.mTargetId, this.customServiceListener, this.mCustomUserInfo);
                    }
                } else {
                    PublicServiceCommandMessage msg1 = new PublicServiceCommandMessage();
                    msg1.setCommand(PublicServiceMenu.PublicServiceMenuItemType.Entry.getMessage());
                    Message message1 = Message.obtain(this.mTargetId, this.mConversationType, msg1);
                    RongIMClient.getInstance().sendMessage(message1, (String)null, (String)null, new IRongCallback.ISendMessageCallback() {
                        public void onAttached(Message message) {
                        }

                        public void onSuccess(Message message) {
                        }

                        public void onError(Message message, ErrorCode errorCode) {
                        }
                    });
                }
            } else {
                boolean msg = this.getActivity() != null && this.getActivity().getIntent().getBooleanExtra("createIfNotExist", true);
                int message = this.getResources().getInteger(io.rong.imkit.R.integer.rc_chatroom_first_pull_message_count);
                if(msg) {
                    RongIMClient.getInstance().joinChatRoom(this.mTargetId, message, new RongIMClient.OperationCallback() {
                        public void onSuccess() {
                            RLog.i("ConversationFragment1", "joinChatRoom onSuccess : " + ConversationFragment1.this.mTargetId);
                        }

                        public void onError(ErrorCode errorCode) {
                            RLog.e("ConversationFragment1", "joinChatRoom onError : " + errorCode);
                            if(ConversationFragment1.this.getActivity() != null) {
                                ConversationFragment1.this.onWarningDialog(ConversationFragment1.this.getString(io.rong.imkit.R.string.rc_join_chatroom_failure));
                            }

                        }
                    });
                } else {
                    RongIMClient.getInstance().joinExistChatRoom(this.mTargetId, message, new RongIMClient.OperationCallback() {
                        public void onSuccess() {
                            RLog.i("ConversationFragment1", "joinExistChatRoom onSuccess : " + ConversationFragment1.this.mTargetId);
                        }

                        public void onError(ErrorCode errorCode) {
                            RLog.e("ConversationFragment1", "joinExistChatRoom onError : " + errorCode);
                            if(ConversationFragment1.this.getActivity() != null) {
                                ConversationFragment1.this.onWarningDialog(ConversationFragment1.this.getString(io.rong.imkit.R.string.rc_join_chatroom_failure));
                            }

                        }
                    });
                }
            }

//            if(this.mEnableMention && (this.mConversationType.equals(ConversationType.DISCUSSION) || this.mConversationType.equals(ConversationType.GROUP))) {
//                RongMentionManager.getInstance().createInstance(this.mConversationType, this.mTargetId, this.mListAdapter, this.mRongExtension.getInputEditText());
//            }

            RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.PUBLIC_SERVICE, this.mTargetId, new ResultCallback<PublicServiceProfile>() {
                public void onSuccess(PublicServiceProfile publicServiceProfile) {
                    ArrayList inputMenuList = new ArrayList();
                    PublicServiceMenu menu = publicServiceProfile.getMenu();
                    ArrayList items = menu != null?menu.getMenuItems():null;
                    if(items != null) {
                        ConversationFragment1.this.mPublicServiceProfile = publicServiceProfile;
                        Iterator i$ = items.iterator();

                        while(i$.hasNext()) {
                            PublicServiceMenuItem item = (PublicServiceMenuItem)i$.next();
                            InputMenu inputMenu = new InputMenu();
                            inputMenu.title = item.getName();
                            inputMenu.subMenuList = new ArrayList();
                            Iterator i$1 = item.getSubMenuItems().iterator();

                            while(i$1.hasNext()) {
                                PublicServiceMenuItem i = (PublicServiceMenuItem)i$1.next();
                                inputMenu.subMenuList.add(i.getName());
                            }

                            inputMenuList.add(inputMenu);
                        }

                        ConversationFragment1.this.mRongExtension.setInputMenu(inputMenuList, true);
                    }

                }

                public void onError(ErrorCode e) {
                }
            });
        }

        RongIMClient.getInstance().getConversation(this.mConversationType, this.mTargetId, new ResultCallback<Conversation>() {
            public void onSuccess(Conversation conversation) {
                if(conversation != null && ConversationFragment1.this.getActivity() != null) {
                    final int unreadCount = conversation.getUnreadMessageCount();
                    if(unreadCount > 0) {
                        ConversationFragment1.this.sendReadReceiptAndSyncUnreadStatus(ConversationFragment1.this.mConversationType, ConversationFragment1.this.mTargetId, conversation.getSentTime());
                    }

                    if(conversation.getMentionedCount() > 0) {
                        ConversationFragment1.this.getLastMentionedMessageId(ConversationFragment1.this.mConversationType, ConversationFragment1.this.mTargetId);
                    }

                    if(unreadCount > 10 && ConversationFragment1.this.mUnreadBtn != null) {
                        if(unreadCount > 150) {
                            ConversationFragment1.this.mUnreadBtn.setText(String.format("%s%s", new Object[]{"150+", ConversationFragment1.this.getActivity().getResources().getString(io.rong.imkit.R.string.rc_new_messages)}));
                        } else {
                            ConversationFragment1.this.mUnreadBtn.setText(String.format("%s%s", new Object[]{Integer.valueOf(unreadCount), ConversationFragment1.this.getActivity().getResources().getString(io.rong.imkit.R.string.rc_new_messages)}));
                        }

                        ConversationFragment1.this.mUnreadBtn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                ConversationFragment1.this.mUnreadBtn.setClickable(false);
                                TranslateAnimation animation = new TranslateAnimation(0.0F, 500.0F, 0.0F, 0.0F);
                                animation.setDuration(500L);
                                ConversationFragment1.this.mUnreadBtn.startAnimation(animation);
                                animation.setFillAfter(true);
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    public void onAnimationEnd(Animation animation) {
                                        ConversationFragment1.this.mUnreadBtn.setVisibility(View.GONE);
                                        if(unreadCount <= 30) {
                                            if(ConversationFragment1.this.mList.getCount() < 30) {
                                                ConversationFragment1.this.mList.smoothScrollToPosition(ConversationFragment1.this.mList.getCount() - unreadCount);
                                            } else {
                                                ConversationFragment1.this.mList.smoothScrollToPosition(30 - unreadCount);
                                            }
                                        } else if(unreadCount > 30) {
                                            ConversationFragment1.this.getHistoryMessage(ConversationFragment1.this.mConversationType, ConversationFragment1.this.mTargetId, unreadCount - 30 - 1, 2);
                                        }

                                    }

                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                            }
                        });
                        TranslateAnimation translateAnimation = new TranslateAnimation(300.0F, 0.0F, 0.0F, 0.0F);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
                        translateAnimation.setDuration(1000L);
                        alphaAnimation.setDuration(2000L);
                        AnimationSet set = new AnimationSet(true);
                        set.addAnimation(translateAnimation);
                        set.addAnimation(alphaAnimation);
                        ConversationFragment1.this.mUnreadBtn.setVisibility(View.VISIBLE);
                        ConversationFragment1.this.mUnreadBtn.startAnimation(set);
                        set.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation animation) {
                            }

                            public void onAnimationEnd(Animation animation) {
                                ConversationFragment1.this.getHandler().postDelayed(new Runnable() {
                                    public void run() {
                                        TranslateAnimation animation = new TranslateAnimation(0.0F, 700.0F, 0.0F, 0.0F);
                                        animation.setDuration(700L);
                                        animation.setFillAfter(true);
                                        ConversationFragment1.this.mUnreadBtn.startAnimation(animation);
                                    }
                                }, 4000L);
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                    }
                }

            }

            public void onError(ErrorCode e) {
            }
        });
        this.getHistoryMessage(this.mConversationType, this.mTargetId, 30, 3);
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    public boolean onResendItemClick(Message message) {
        return false;
    }

    public void onReadReceiptStateClick(Message message) {
    }

    public void onSelectCustomerServiceGroup(final List<CSGroupItem> groupList) {
        if(this.getActivity() != null) {
            ArrayList singleDataList = new ArrayList();
            singleDataList.clear();

            for(int i = 0; i < groupList.size(); ++i) {
                if(((CSGroupItem)groupList.get(i)).getOnline()) {
                    singleDataList.add(((CSGroupItem)groupList.get(i)).getName());
                }
            }

            if(singleDataList.size() == 0) {
                RongIMClient.getInstance().selectCustomServiceGroup(this.mTargetId, (String)null);
            } else {
                final SingleChoiceDialog singleChoiceDialog = new SingleChoiceDialog(this.getActivity(), singleDataList);
                singleChoiceDialog.setTitle(this.getActivity().getResources().getString(io.rong.imkit.R.string.rc_cs_select_group));
                singleChoiceDialog.setOnOKButtonListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int selItem = singleChoiceDialog.getSelectItem();
                        RongIMClient.getInstance().selectCustomServiceGroup(ConversationFragment1.this.mTargetId, ((CSGroupItem)groupList.get(selItem)).getId());
                    }
                });
                singleChoiceDialog.setOnCancelButtonListener(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RongIMClient.getInstance().selectCustomServiceGroup(ConversationFragment1.this.mTargetId, (String)null);
                    }
                });
                singleChoiceDialog.show();
            }
        }
    }

    public void onPause() {
        if(this.getActivity().isFinishing()) {
            this.destroyExtension();
        }

        super.onPause();
    }

    private void destroyExtension() {
        String text = this.mRongExtension.getInputEditText().getText().toString();
        if(TextUtils.isEmpty(text) && !TextUtils.isEmpty(this.mDraft) || !TextUtils.isEmpty(text) && TextUtils.isEmpty(this.mDraft) || !TextUtils.isEmpty(text) && !TextUtils.isEmpty(this.mDraft) && !text.equals(this.mDraft)) {
            RongIMClient.getInstance().saveTextMessageDraft(this.mConversationType, this.mTargetId, text, (ResultCallback)null);
            Event.DraftEvent draft = new Event.DraftEvent(this.mConversationType, this.mTargetId, text);
            RongContext.getInstance().getEventBus().post(draft);
        }

        this.mRongExtension.onDestroy();
        this.mRongExtension = null;
    }

    public void onDestroy() {
        if(this.mConversationType.equals(ConversationType.CHATROOM)) {
            SendImageManager.getInstance().cancelSendingImages(this.mConversationType, this.mTargetId);
            RongIM.getInstance().quitChatRoom(this.mTargetId, (RongIMClient.OperationCallback)null);
        }

        if(this.mConversationType.equals(ConversationType.CUSTOMER_SERVICE) && this.mCSneedToQuit) {
            RongIMClient.getInstance().stopCustomService(this.mTargetId);
        }

        if(this.mSyncReadStatus && this.mSyncReadStatusMsgTime > 0L && this.mConversationType.equals(ConversationType.DISCUSSION) || this.mConversationType.equals(ConversationType.GROUP) || this.mConversationType.equals(ConversationType.PRIVATE)) {
            RongIMClient.getInstance().syncConversationReadStatus(this.mConversationType, this.mTargetId, this.mSyncReadStatusMsgTime, (RongIMClient.OperationCallback)null);
        }

        if(this.mEnableMention && (this.mConversationType.equals(ConversationType.DISCUSSION) || this.mConversationType.equals(ConversationType.GROUP))) {
//            RongMentionManager.getInstance().destroyInstance();
        }

        RongIM.getInstance().clearMessagesUnreadStatus(this.mConversationType, this.mTargetId, (ResultCallback)null);
        EventBus.getDefault().unregister(this);
        AudioPlayManager.getInstance().stopPlay();
        AudioRecordManager.getInstance().stopRecord();
        RongContext.getInstance().unregisterConversationInfo(this.mCurrentConversationInfo);
        LocationManager.getInstance().quitLocationSharing();
//        LocationManager.getInstance().setParticipantChangedListener((IParticipantChangedListener)null);
        LocationManager.getInstance().setUserInfoProvider((IUserInfoProvider)null);
        LocationManager.getInstance().unBindConversation();
        super.onDestroy();
    }

    public boolean isLocationSharing() {
        return LocationManager.getInstance().isSharing();
    }

    public void showQuitLocationSharingDialog(final Activity activity) {
//        PromptPopupDialog.newInstance(activity, this.getString(io.rong.imkit.R.string.rc_ext_warning), this.getString(io.rong.imkit.R.string.rc_ext_exit_location_sharing)).setPromptButtonClickedListener(new PromptPopupDialog.OnPromptButtonClickedListener() {
//            public void onPositiveButtonClicked() {
//                activity.finish();
//            }
//        }).show();
    }

    public boolean onBackPressed() {
        if(this.mRongExtension.isExtensionExpanded()) {
            this.mRongExtension.collapseExtension();
            return true;
        } else {
            return this.mConversationType != null && this.mConversationType.equals(ConversationType.CUSTOMER_SERVICE)?this.onCustomServiceEvaluation(false, "", this.robotType, this.evaluate):false;
        }
    }

    public boolean handleMessage(android.os.Message msg) {
        return false;
    }

    public void onWarningDialog(String msg) {
        if(this.getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setCancelable(false);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(io.rong.imkit.R.layout.rc_cs_alert_warning);
            TextView tv = (TextView)window.findViewById(io.rong.imkit.R.id.rc_cs_msg);
            tv.setText(msg);
            window.findViewById(io.rong.imkit.R.id.rc_btn_ok).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    alertDialog.dismiss();
                    FragmentManager fm = ConversationFragment1.this.getChildFragmentManager();
                    if(fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    } else {
                        ConversationFragment1.this.getActivity().finish();
                    }

                }
            });
        }
    }

    public void onCustomServiceWarning(String msg, final boolean evaluate) {
        if(this.getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setCancelable(false);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(io.rong.imkit.R.layout.rc_cs_alert_warning);
            TextView tv = (TextView)window.findViewById(io.rong.imkit.R.id.rc_cs_msg);
            tv.setText(msg);
            window.findViewById(io.rong.imkit.R.id.rc_btn_ok).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if(evaluate) {
                        ConversationFragment1.this.onCustomServiceEvaluation(false, "", ConversationFragment1.this.robotType, evaluate);
                    } else {
                        FragmentManager fm = ConversationFragment1.this.getChildFragmentManager();
                        if(fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        } else {
                            ConversationFragment1.this.getActivity().finish();
                        }
                    }

                }
            });
        }
    }

    public boolean onCustomServiceEvaluation(boolean isPullEva, final String dialogId, final boolean robotType, boolean evaluate) {
        if(!evaluate) {
            return false;
        } else if(this.getActivity() == null) {
            return false;
        } else {
            long currentTime = System.currentTimeMillis();
            int interval = 60;

            try {
                interval = RongContext.getInstance().getResources().getInteger(io.rong.imkit.R.integer.rc_custom_service_evaluation_interval);
            } catch (Resources.NotFoundException var14) {
                var14.printStackTrace();
            }

            if(currentTime - this.enterTime < (long)(interval * 1000) && !isPullEva) {
                //"input_method"
                InputMethodManager var15 = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(var15 != null && var15.isActive() && this.getActivity().getCurrentFocus() != null && this.getActivity().getCurrentFocus().getWindowToken() != null) {
                    var15.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), 2);
                }

                FragmentManager var16 = this.getChildFragmentManager();
                if(var16.getBackStackEntryCount() > 0) {
                    var16.popBackStack();
                } else {
                    this.getActivity().finish();
                }

                return false;
            } else {
                this.committing = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                final LinearLayout linearLayout;
                int i;
                View child;
                if(robotType) {
                    window.setContentView(io.rong.imkit.R.layout.rc_cs_alert_robot_evaluation);
                    linearLayout = (LinearLayout)window.findViewById(io.rong.imkit.R.id.rc_cs_yes_no);
                    if(this.resolved) {
                        linearLayout.getChildAt(0).setSelected(true);
                        linearLayout.getChildAt(1).setSelected(false);
                    } else {
                        linearLayout.getChildAt(0).setSelected(false);
                        linearLayout.getChildAt(1).setSelected(true);
                    }

                    for(i = 0; i < linearLayout.getChildCount(); ++i) {
                        child = linearLayout.getChildAt(i);
                        child.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                v.setSelected(true);
                                int index = linearLayout.indexOfChild(v);
                                if(index == 0) {
                                    linearLayout.getChildAt(1).setSelected(false);
                                    ConversationFragment1.this.resolved = true;
                                } else {
                                    ConversationFragment1.this.resolved = false;
                                    linearLayout.getChildAt(0).setSelected(false);
                                }

                            }
                        });
                    }
                } else {
                    window.setContentView(io.rong.imkit.R.layout.rc_cs_alert_human_evaluation);
                    linearLayout = (LinearLayout)window.findViewById(io.rong.imkit.R.id.rc_cs_stars);

                    for(i = 0; i < linearLayout.getChildCount(); ++i) {
                        child = linearLayout.getChildAt(i);
                        if(i < this.source) {
                            child.setSelected(true);
                        }

                        child.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                int index = linearLayout.indexOfChild(v);
                                int count = linearLayout.getChildCount();
                                ConversationFragment1.this.source = index + 1;
                                if(!v.isSelected()) {
                                    while(index >= 0) {
                                        linearLayout.getChildAt(index).setSelected(true);
                                        --index;
                                    }
                                } else {
                                    ++index;

                                    while(index < count) {
                                        linearLayout.getChildAt(index).setSelected(false);
                                        ++index;
                                    }
                                }

                            }
                        });
                    }
                }

                window.findViewById(io.rong.imkit.R.id.rc_btn_cancel).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ConversationFragment1.this.committing = false;
                        alertDialog.dismiss();
                        FragmentManager fm = ConversationFragment1.this.getChildFragmentManager();
                        if(fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        } else {
                            ConversationFragment1.this.getActivity().finish();
                        }

                    }
                });
                window.findViewById(io.rong.imkit.R.id.rc_btn_ok).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(robotType) {
                            RongIMClient.getInstance().evaluateCustomService(ConversationFragment1.this.mTargetId, ConversationFragment1.this.resolved, "");
                        } else if(ConversationFragment1.this.source > 0) {
                            RongIMClient.getInstance().evaluateCustomService(ConversationFragment1.this.mTargetId, ConversationFragment1.this.source, (String)null, dialogId);
                        }

                        alertDialog.dismiss();
                        ConversationFragment1.this.committing = false;
                        FragmentManager fm = ConversationFragment1.this.getChildFragmentManager();
                        if(fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        } else {
                            ConversationFragment1.this.getActivity().finish();
                        }

                    }
                });
                return true;
            }
        }
    }

    public void onSendToggleClick(View v, String text) {
        if(!TextUtils.isEmpty(this.mTargetId) && this.mConversationType != null) {
            if(!TextUtils.isEmpty(text) && !TextUtils.isEmpty(text.trim())) {
                TextMessage textMessage = TextMessage.obtain(text);
                MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();
                if(mentionedInfo != null) {
                    textMessage.setMentionedInfo(mentionedInfo);
                }
                Message message = Message.obtain(this.mTargetId, this.mConversationType, textMessage);
                setMessageData(message);
                RongIM.getInstance().sendMessage(message, (String)null, (String)null, (RongIMClient.SendMessageCallback) null);
            } else {
                RLog.e("ConversationFragment1", "text content must not be null");
            }
        } else {
            Log.e("InputProvider", "the conversation hasn\'t been created yet!!!");
        }
    }

    private void setMessageData(Message message){
        if(message != null){
            message.setSentStatus(SentStatus.SENT);
            ResultCallback.Result result = new ResultCallback.Result();
//                RongIM.getInstance().sendMessage(message, (String) null, (String) null,(ISendMediaMessageCallback)null);
            message.setObjectName("RC:TxtMsg");
            message.setSentTime(System.currentTimeMillis());
//            message.setSenderUserId(BaseApplication.getmUserModel().getCustomerNo());
            message.setReceivedStatus(new ReceivedStatus(1));
            message.setMessageDirection(MessageDirection.SEND);
        }
    }

    public void onImageResult(List<Uri> selectedImages, boolean origin) {
        SendImageManager.getInstance().sendImages(this.mConversationType, this.mTargetId, selectedImages, origin);
        if(this.mConversationType.equals(ConversationType.PRIVATE)) {
            RongIMClient.getInstance().sendTypingStatus(this.mConversationType, this.mTargetId, "RC:ImgMsg");
        }

    }

    public void onEditTextClick(EditText editText) {
    }

    public void onLocationResult(double lat, double lng, String poi, Uri thumb) {
        LocationMessage locationMessage = LocationMessage.obtain(lat, lng, poi, thumb);
        Message message = Message.obtain(this.mTargetId, this.mConversationType, locationMessage);
        setMessageData(message);
//        RongIM.getInstance().sendLocationMessage(message, (String)null, (String)null, (IRongCallback.ISendMessageCallback)null);
        RongIM.getInstance().sendLocationMessage(message, (String) null, (String) null, new ISendMediaMessageCallback() {
            @Override
            public void onProgress(Message message, int i) {

            }

            @Override
            public void onCanceled(Message message) {

            }

            @Override
            public void onAttached(Message message) {
                message.setSentStatus(SentStatus.SENT);
            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, ErrorCode errorCode) {

            }
        });
        if(this.mConversationType.equals(ConversationType.PRIVATE)) {
            RongIMClient.getInstance().sendTypingStatus(this.mConversationType, this.mTargetId, "RC:LBSMsg");
        }

    }

    public void onSwitchToggleClick(View v, ViewGroup inputBoard) {
        if(this.robotType) {
            RongIMClient.getInstance().switchToHumanMode(this.mTargetId);
        }

    }

    public void onVoiceInputToggleTouch(View v, MotionEvent event) {
        String[] permissions = new String[]{"android.permission.RECORD_AUDIO"};
        if(!PermissionCheckUtil.checkPermissions(this.getActivity(), permissions)) {
            if(event.getAction() == 0) {
                PermissionCheckUtil.requestPermissions(this, permissions, 100);
            }

        } else {
            if(event.getAction() == 0) {
                AudioPlayManager.getInstance().stopPlay();
                AudioRecordManager.getInstance().startRecord(v.getRootView(), this.mConversationType, this.mTargetId);
                this.mLastTouchY = event.getY();
                this.mUpDirection = false;
                ((Button)v).setText(io.rong.imkit.R.string.rc_audio_input_hover);
            } else if(event.getAction() == 2) {
                if(this.mLastTouchY - event.getY() > this.mOffsetLimit && !this.mUpDirection) {
                    AudioRecordManager.getInstance().willCancelRecord();
                    this.mUpDirection = true;
                    ((Button)v).setText(io.rong.imkit.R.string.rc_audio_input);
                } else if(event.getY() - this.mLastTouchY > -this.mOffsetLimit && this.mUpDirection) {
                    AudioRecordManager.getInstance().continueRecord();
                    this.mUpDirection = false;
                    ((Button)v).setText(io.rong.imkit.R.string.rc_audio_input_hover);
                }
            } else if(event.getAction() == 1 || event.getAction() == 3) {
                AudioRecordManager.getInstance().stopRecord();
                ((Button)v).setText(io.rong.imkit.R.string.rc_audio_input);
            }

            if(this.mConversationType.equals(ConversationType.PRIVATE)) {
                RongIMClient.getInstance().sendTypingStatus(this.mConversationType, this.mTargetId, "RC:VcMsg");
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults[0] != 0) {
            Toast.makeText(this.getActivity(), this.getResources().getString(io.rong.imkit.R.string.rc_permission_grant_needed), Toast.LENGTH_SHORT).show();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onEmoticonToggleClick(View v, ViewGroup extensionBoard) {
    }

    public void onPluginToggleClick(View v, ViewGroup extensionBoard) {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int cursor;
        int offset;
        if(count == 0) {
            cursor = start + before;
            offset = -before;
        } else {
            cursor = start;
            offset = count;
        }

        if(!this.mConversationType.equals(ConversationType.GROUP) && !this.mConversationType.equals(ConversationType.DISCUSSION)) {
            if(this.mConversationType.equals(ConversationType.PRIVATE) && offset != 0) {
                RongIMClient.getInstance().sendTypingStatus(this.mConversationType, this.mTargetId, "RC:TxtMsg");
            }
        } else {
            RongMentionManager.getInstance().onTextEdit(this.mConversationType, this.mTargetId, cursor, offset, s.toString());
        }

    }

    public void afterTextChanged(Editable s) {

    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(event.getKeyCode() == 67 && event.getAction() == 0) {
            EditText editText = (EditText)v;
            int cursorPos = editText.getSelectionStart();
            RongMentionManager.getInstance().onDeleteClick(this.mConversationType, this.mTargetId, editText, cursorPos);
        }
        return false;
    }

    public void onMenuClick(int root, int sub) {
        if(this.mPublicServiceProfile != null) {
            PublicServiceMenuItem item = (PublicServiceMenuItem)this.mPublicServiceProfile.getMenu().getMenuItems().get(root);
            if(sub >= 0) {
                item = (PublicServiceMenuItem)item.getSubMenuItems().get(sub);
            }
//            if(item.getType().equals(PublicServiceMenu.PublicServiceMenuItemType.View)) {
//                IPublicServiceMenuClickListener msg = RongContext.getInstance().getPublicServiceMenuClickListener();
//                if(msg == null || !msg.onClick(this.mConversationType, this.mTargetId, item)) {
//                    String action = "io.rong.imkit.intent.action.webview";
//                    Intent intent = new Intent(action);
//                    intent.setPackage(this.getActivity().getPackageName());
//                    intent.addFlags(268435456);
//                    intent.putExtra("url", item.getUrl());
//                    this.getActivity().startActivity(intent);
//                }
//            }
            PublicServiceCommandMessage msg1 = PublicServiceCommandMessage.obtain(item);
            RongIMClient.getInstance().sendMessage(this.mConversationType, this.mTargetId, msg1, (String)null, (String)null, new IRongCallback.ISendMessageCallback() {
                public void onAttached(Message message) {
                }

                public void onSuccess(Message message) {
                }

                public void onError(Message message, ErrorCode errorCode) {
                }
            });
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mRongExtension.onActivityPluginResult(requestCode, resultCode, data);
    }
    public void onExtensionCollapsed() {
    }
    public void onExtensionExpanded(int h) {
        this.mList.setTranscriptMode(2);
        this.mList.smoothScrollToPosition(this.mList.getCount());
    }

    public void onPluginClicked(IPluginModule pluginModule, int position) {
    }

    private String getNameFromCache(String targetId) {
        UserInfo info = RongContext.getInstance().getUserInfoFromCache(targetId);
        return info == null?targetId:info.getName();
    }

    public final void onEventMainThread(Event.ReadReceiptRequestEvent event) {
        RLog.d("ConversationFragment1", "ReadReceiptRequestEvent");
        if((this.mConversationType.equals(ConversationType.GROUP) || this.mConversationType.equals(ConversationType.DISCUSSION)) && RongContext.getInstance().isReadReceiptConversationType(event.getConversationType()) && event.getConversationType().equals(this.mConversationType) && event.getTargetId().equals(this.mTargetId)) {
            for(int i = 0; i < this.mListAdapter.getCount(); ++i) {
                if(((UIMessage)this.mListAdapter.getItem(i)).getUId().equals(event.getMessageUId())) {
                    final UIMessage uiMessage = (UIMessage)this.mListAdapter.getItem(i);
                    ReadReceiptInfo readReceiptInfo = uiMessage.getReadReceiptInfo();
                    if(readReceiptInfo == null) {
                        readReceiptInfo = new ReadReceiptInfo();
                        uiMessage.setReadReceiptInfo(readReceiptInfo);
                    }

                    if(readReceiptInfo.isReadReceiptMessage() && readReceiptInfo.hasRespond()) {
                        return;
                    }

                    readReceiptInfo.setIsReadReceiptMessage(true);
                    readReceiptInfo.setHasRespond(false);
                    ArrayList messageList = new ArrayList();
                    messageList.add(((UIMessage)this.mListAdapter.getItem(i)).getMessage());
                    RongIMClient.getInstance().sendReadReceiptResponse(event.getConversationType(), event.getTargetId(), messageList, new RongIMClient.OperationCallback() {
                        public void onSuccess() {
                            uiMessage.getReadReceiptInfo().setHasRespond(true);
                        }

                        public void onError(ErrorCode errorCode) {
                            RLog.e("ConversationFragment1", "sendReadReceiptResponse failed, errorCode = " + errorCode);
                        }
                    });
                    break;
                }
            }
        }

    }

    public final void onEventMainThread(Event.ReadReceiptResponseEvent event) {
        RLog.d("ConversationFragment1", "ReadReceiptResponseEvent");
        if((this.mConversationType.equals(ConversationType.GROUP) || this.mConversationType.equals(ConversationType.DISCUSSION)) && RongContext.getInstance().isReadReceiptConversationType(event.getConversationType()) && event.getConversationType().equals(this.mConversationType) && event.getTargetId().equals(this.mTargetId)) {
            for(int i = 0; i < this.mListAdapter.getCount(); ++i) {
                if(((UIMessage)this.mListAdapter.getItem(i)).getUId().equals(event.getMessageUId())) {
                    UIMessage uiMessage = (UIMessage)this.mListAdapter.getItem(i);
                    ReadReceiptInfo readReceiptInfo = uiMessage.getReadReceiptInfo();
                    if(readReceiptInfo == null) {
                        readReceiptInfo = new ReadReceiptInfo();
                        readReceiptInfo.setIsReadReceiptMessage(true);
                        uiMessage.setReadReceiptInfo(readReceiptInfo);
                    }

                    readReceiptInfo.setRespondUserIdList(event.getResponseUserIdList());
                    int first = this.getFirstVisiblePositionInAdapter();
                    int last = this.getLastVisiblePositionInAdapter();
                    if(i >= first && i <= last) {
                        this.mListAdapter.getView(i, this.getListViewChildAt(i), this.mList);
                    }
                    break;
                }
            }
        }

    }

    public final void onEventMainThread(Event.MessageDeleteEvent deleteEvent) {
        RLog.d("ConversationFragment1", "MessageDeleteEvent");
        if(deleteEvent.getMessageIds() != null) {
            Iterator i$ = deleteEvent.getMessageIds().iterator();

            while(i$.hasNext()) {
                long messageId = (long)((Integer)i$.next()).intValue();
                int position = this.mListAdapter.findPosition(messageId);
                if(position >= 0) {
                    this.mListAdapter.remove(position);
                }
            }

            this.mListAdapter.notifyDataSetChanged();
        }

    }

    public final void onEventMainThread(Event.PublicServiceFollowableEvent event) {
        RLog.d("ConversationFragment1", "PublicServiceFollowableEvent");
        if(event != null && !event.isFollow()) {
            this.getActivity().finish();
        }

    }

    public final void onEventMainThread(Event.MessagesClearEvent clearEvent) {
        RLog.d("ConversationFragment1", "MessagesClearEvent");
        if(clearEvent.getTargetId().equals(this.mTargetId) && clearEvent.getType().equals(this.mConversationType)) {
            this.mListAdapter.clear();
            this.mListAdapter.notifyDataSetChanged();
        }

    }

    public final void onEventMainThread(Event.MessageRecallEvent event) {
        RLog.d("ConversationFragment1", "MessageRecallEvent");
        if(event.isRecallSuccess()) {
            RecallNotificationMessage recallNotificationMessage = event.getRecallNotificationMessage();
            int position = this.mListAdapter.findPosition((long)event.getMessageId());
            if(position != -1) {
                ((UIMessage)this.mListAdapter.getItem(position)).setContent(recallNotificationMessage);
                int first = this.getFirstVisiblePositionInAdapter();
                int last = this.getLastVisiblePositionInAdapter();
                if(position >= first && position <= last) {
                    this.mListAdapter.getView(position, this.getListViewChildAt(position), this.mList);
                }
            }
        } else {
            Toast.makeText(this.getActivity(), io.rong.imkit.R.string.rc_recall_failed, Toast.LENGTH_SHORT).show();
        }

    }

    public final void onEventMainThread(Event.RemoteMessageRecallEvent event) {
        RLog.d("ConversationFragment1", "RemoteMessageRecallEvent");
        int position = this.mListAdapter.findPosition((long)event.getMessageId());
        int first = this.getFirstVisiblePositionInAdapter();
        int last = this.getLastVisiblePositionInAdapter();
        if(position >= 0) {
            UIMessage uiMessage = (UIMessage)this.mListAdapter.getItem(position);
            if(uiMessage.getMessage().getContent() instanceof VoiceMessage) {
                AudioPlayManager.getInstance().stopPlay();
            }

            uiMessage.setContent(event.getRecallNotificationMessage());
            if(position >= first && position <= last) {
                this.mListAdapter.getView(position, this.getListViewChildAt(position), this.mList);
            }
        }

    }

    public final void onEventMainThread(Message msg) {
        RLog.d("ConversationFragment1", "Event message : " + msg.getMessageId() + ", " + msg.getObjectName() + ", " + msg.getSentStatus());
        if(this.mTargetId.equals(msg.getTargetId()) && this.mConversationType.equals(msg.getConversationType())) {
            int position = this.mListAdapter.findPosition((long)msg.getMessageId());
            if(position >= 0) {
                ((UIMessage)this.mListAdapter.getItem(position)).setMessage(msg);
                this.mListAdapter.getView(position, this.getListViewChildAt(position), this.mList);
            } else {
                this.mListAdapter.add(UIMessage.obtain(msg));
                this.mListAdapter.notifyDataSetChanged();
            }

            if(msg.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId()) && this.mList.getLastVisiblePosition() - 1 != this.mList.getCount()) {
                this.mList.smoothScrollToPosition(this.mList.getCount());
            }
        }

    }

    public final void onEventMainThread(Event.FileMessageEvent event) {
        this.onEventMainThread(event.getMessage());
    }

    public void onEventMainThread(GroupUserInfo groupUserInfo) {
        RLog.d("ConversationFragment1", "GroupUserInfoEvent " + groupUserInfo.getGroupId() + " " + groupUserInfo.getUserId() + " " + groupUserInfo.getNickname());
        if(groupUserInfo.getNickname() != null && groupUserInfo.getGroupId() != null) {
            int count = this.mListAdapter.getCount();
            int first = this.getFirstVisiblePositionInAdapter();
            int last = this.getLastVisiblePositionInAdapter();

            for(int i = 0; i < count; ++i) {
                UIMessage uiMessage = (UIMessage)this.mListAdapter.getItem(i);
                if(uiMessage.getSenderUserId().equals(groupUserInfo.getUserId())) {
                    uiMessage.setNickName(true);
                    UserInfo userInfo = uiMessage.getUserInfo();
                    userInfo.setName(groupUserInfo.getNickname());
                    uiMessage.setUserInfo(userInfo);
                    if(i >= first && i <= last) {
                        this.mListAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                }
            }
        }
    }

    private int getFirstVisiblePositionInAdapter() {
        int first = this.mList.getFirstVisiblePosition();
        int header = this.mList.getHeaderViewsCount();
        return first == 0 && header > 0?0:first - header;
    }

    private int getLastVisiblePositionInAdapter() {
        int last = this.mList.getLastVisiblePosition();
        int header = this.mList.getHeaderViewsCount();
        return last - header < 0?0:last - header;
    }

    private View getListViewChildAt(int adapterIndex) {
        int header = this.mList.getHeaderViewsCount();
        int first = this.mList.getFirstVisiblePosition();
        return this.mList.getChildAt(adapterIndex + header - first);
    }

    public final void onEventMainThread(Event.OnMessageSendErrorEvent event) {
        this.onEventMainThread(event.getMessage());
    }

    public final void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Message message = event.getMessage();
        RLog.i("ConversationFragment1", "OnReceiveMessageEvent, " + message.getMessageId() + ", " + message.getObjectName() + ", " + message.getReceivedStatus());
        ConversationType conversationType = message.getConversationType();
        String targetId = message.getTargetId();
        if(this.mConversationType.equals(conversationType) && this.mTargetId.equals(targetId)) {
            if(event.getLeft() == 0 && message.getConversationType().equals(ConversationType.PRIVATE) && RongContext.getInstance().isReadReceiptConversationType(ConversationType.PRIVATE) && message.getMessageDirection().equals(MessageDirection.RECEIVE)) {
                if(this.mReadRec) {
                    RongIMClient.getInstance().sendReadReceiptMessage(message.getConversationType(), message.getTargetId(), message.getSentTime());
                } else if(this.mSyncReadStatus) {
                    RongIMClient.getInstance().syncConversationReadStatus(message.getConversationType(), message.getTargetId(), message.getSentTime(), (RongIMClient.OperationCallback)null);
                }
            }

            if(this.mSyncReadStatus) {
                this.mSyncReadStatusMsgTime = message.getSentTime();
            }

            if(message.getMessageId() > 0) {
                ReceivedStatus status = message.getReceivedStatus();
                status.setRead();
                message.setReceivedStatus(status);
                RongIMClient.getInstance().setMessageReceivedStatus(message.getMessageId(), status, (ResultCallback)null);
            }

            if(this.mNewMessageBtn != null && this.mList.getCount() - this.mList.getLastVisiblePosition() > 2 && MessageDirection.SEND != message.getMessageDirection() && message.getConversationType() != ConversationType.CHATROOM && message.getConversationType() != ConversationType.CUSTOMER_SERVICE && message.getConversationType() != ConversationType.APP_PUBLIC_SERVICE && message.getConversationType() != ConversationType.PUBLIC_SERVICE) {
                ++this.mNewMessageCount;
                if(this.mNewMessageCount > 0) {
                    this.mNewMessageBtn.setVisibility(View.VISIBLE);
                    this.mNewMessageTextView.setVisibility(View.VISIBLE);
                }

                if(this.mNewMessageCount > 99) {
                    this.mNewMessageTextView.setText("99+");
                } else {
                    this.mNewMessageTextView.setText(this.mNewMessageCount + "");
                }
            }

            this.onEventMainThread(event.getMessage());
        }

    }

    public final void onEventMainThread(Event.PlayAudioEvent event) {
        RLog.i("ConversationFragment1", "PlayAudioEvent");
        int first = this.getFirstVisiblePositionInAdapter();

        int last;
//        UIMessage uiMessage;
//        for(last = this.getLastVisiblePositionInAdapter(); first <= last; ++first) {
//            uiMessage = (UIMessage)this.mListAdapter.getItem(first);
//            if(uiMessage.getContent().equals(event.content)) {
//                uiMessage.continuePlayAudio = false;
//                break;
//            }
//        }
//
//        ++first;
//        if(event.continuously) {
//            while(first <= last) {
//                uiMessage = (UIMessage)this.mListAdapter.getItem(first);
//                if(uiMessage.getContent() instanceof VoiceMessage && uiMessage.getMessageDirection().equals(MessageDirection.RECEIVE) && !uiMessage.getReceivedStatus().isListened()) {
//                    uiMessage.continuePlayAudio = true;
//                    this.mListAdapter.getView(first, this.getListViewChildAt(first), this.mList);
//                    break;
//                }
//
//                ++first;
//            }
//        }

    }

    public final void onEventMainThread(Event.OnReceiveMessageProgressEvent event) {
        if(this.mList != null) {
            int first = this.getFirstVisiblePositionInAdapter();

            for(int last = this.getLastVisiblePositionInAdapter(); first <= last; ++first) {
                UIMessage uiMessage = (UIMessage)this.mListAdapter.getItem(first);
                if(uiMessage.getMessageId() == event.getMessage().getMessageId()) {
                    uiMessage.setProgress(event.getProgress());
                    if(this.isResumed()) {
                        this.mListAdapter.getView(first, this.getListViewChildAt(first), this.mList);
                    }
                    break;
                }
            }
        }

    }

    public final void onEventMainThread(UserInfo userInfo) {
        RLog.i("ConversationFragment1", "userInfo " + userInfo.getUserId());
        int first = this.getFirstVisiblePositionInAdapter();
        int last = this.getLastVisiblePositionInAdapter();

        for(int i = 0; i < this.mListAdapter.getCount(); ++i) {
            UIMessage uiMessage = (UIMessage)this.mListAdapter.getItem(i);
            if(userInfo.getUserId().equals(uiMessage.getSenderUserId()) && !uiMessage.isNickName()) {
                if(uiMessage.getConversationType().equals(ConversationType.CUSTOMER_SERVICE) && uiMessage.getMessage() != null && uiMessage.getMessage().getContent() != null && uiMessage.getMessage().getContent().getUserInfo() != null) {
                    uiMessage.setUserInfo(uiMessage.getMessage().getContent().getUserInfo());
                } else {
                    uiMessage.setUserInfo(userInfo);
                }

                if(i >= first && i <= last) {
                    this.mListAdapter.getView(i, this.mList.getChildAt(i - first), this.mList);
                }
            }
        }

    }

    public final void onEventMainThread(PublicServiceProfile publicServiceProfile) {
        RLog.i("ConversationFragment1", "publicServiceProfile");
        int first = this.getFirstVisiblePositionInAdapter();

        for(int last = this.getLastVisiblePositionInAdapter(); first <= last; ++first) {
            UIMessage message = (UIMessage)this.mListAdapter.getItem(first);
            if(message != null && (TextUtils.isEmpty(message.getTargetId()) || publicServiceProfile.getTargetId().equals(message.getTargetId()))) {
                this.mListAdapter.getView(first, this.getListViewChildAt(first), this.mList);
            }
        }

    }

    public final void onEventMainThread(Event.ReadReceiptEvent event) {
        RLog.i("ConversationFragment1", "ReadReceiptEvent");
        if(RongContext.getInstance().isReadReceiptConversationType(event.getMessage().getConversationType()) && this.mTargetId.equals(event.getMessage().getTargetId()) && this.mConversationType.equals(event.getMessage().getConversationType()) && event.getMessage().getMessageDirection().equals(MessageDirection.RECEIVE)) {
            ReadReceiptMessage content = (ReadReceiptMessage)event.getMessage().getContent();
            long ntfTime = content.getLastMessageSendTime();

            for(int i = this.mListAdapter.getCount() - 1; i >= 0; --i) {
                UIMessage uiMessage = (UIMessage)this.mListAdapter.getItem(i);
                if(uiMessage.getSentStatus() == SentStatus.SENT && uiMessage.getMessageDirection().equals(MessageDirection.SEND) && ntfTime >= uiMessage.getSentTime()) {
                    uiMessage.setSentStatus(SentStatus.READ);
                    int first = this.getFirstVisiblePositionInAdapter();
                    int last = this.getLastVisiblePositionInAdapter();
                    if(i >= first && i <= last) {
                        this.mListAdapter.getView(i, this.getListViewChildAt(i), this.mList);
                    }
                }
            }
        }

    }

    public MessageListAdapter getMessageAdapter() {
        return this.mListAdapter;
    }

    public void getHistoryMessage(ConversationType conversationType, String targetId, final int reqCount, final int scrollMode) {
        this.mList.onRefreshStart(AutoRefreshListView.Mode.START);
        int last = this.mListAdapter.getCount() == 0?-1:((UIMessage)this.mListAdapter.getItem(0)).getMessageId();
        RongIMClient.getInstance().getHistoryMessages(conversationType, targetId, last, reqCount, new ResultCallback<List<Message> >() {
            public void onSuccess(List<Message> messages) {
                RLog.i("ConversationFragment1", "getHistoryMessage " + messages.size());
                ConversationFragment1.this.mHasMoreLocalMessages = messages.size() == reqCount;
                ConversationFragment1.this.mList.onRefreshComplete(reqCount, reqCount, false);
                if(messages.size() > 0) {
                    Iterator index = messages.iterator();
                    while(index.hasNext()) {
                        Message message = (Message)index.next();
                        boolean contains = false;

                        for(int uiMessage = 0; uiMessage < ConversationFragment1.this.mListAdapter.getCount(); ++uiMessage) {
                            contains = ((UIMessage) ConversationFragment1.this.mListAdapter.getItem(uiMessage)).getMessageId() == message.getMessageId();
                        }

                        if(!contains) {
                            UIMessage var7 = UIMessage.obtain(message);
                            ConversationFragment1.this.mListAdapter.add(var7, 0);
                        }
                    }

                    if(scrollMode == 3) {
                        ConversationFragment1.this.mList.setTranscriptMode(2);
                    } else {
                        ConversationFragment1.this.mList.setTranscriptMode(0);
                    }

                    ConversationFragment1.this.mListAdapter.notifyDataSetChanged();
                    if(ConversationFragment1.this.mLastMentionMsgId > 0) {
                        int var6 = ConversationFragment1.this.mListAdapter.findPosition((long) ConversationFragment1.this.mLastMentionMsgId);
                        ConversationFragment1.this.mList.smoothScrollToPosition(var6);
                        ConversationFragment1.this.mLastMentionMsgId = 0;
                    } else if(2 == scrollMode) {
                        ConversationFragment1.this.mList.setSelection(0);
                    } else if(scrollMode == 3) {
                        ConversationFragment1.this.mList.setSelection(ConversationFragment1.this.mList.getCount());
                    } else {
                        ConversationFragment1.this.mList.setSelection(messages.size() + 1);
                    }

                    ConversationFragment1.this.sendReadReceiptResponseIfNeeded(messages);
                }

            }

            public void onError(ErrorCode e) {
                ConversationFragment1.this.mList.onRefreshComplete(reqCount, reqCount, false);
                RLog.e("ConversationFragment1", "getHistoryMessages " + e);
            }
        });
    }

    public void getRemoteHistoryMessages(ConversationType conversationType, String targetId, final int reqCount) {
        this.mList.onRefreshStart(AutoRefreshListView.Mode.START);
//        if(this.mConversationType.equals(ConversationType.CHATROOM)) {
//            this.mList.onRefreshComplete(0, 0, false);
//        } else {
//            page++;
//            long lastTime = this.mListAdapter.getCount() == 0?System.currentTimeMillis():((UIMessage)this.mListAdapter.getItem(0)).getSentTime();
////            TimeUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss")
//            if(conversationType == ConversationType.PRIVATE){
//                doGet(targetId,ConversationType.PRIVATE,BaseApplication.getmUserModel().getCustomerNo(),TimeUtils.getTime(lastTime,TimeUtils.TIME_FORMAT_1));
//            }else {
//                doGet(targetId,ConversationType.GROUP,"",TimeUtils.getTime(lastTime,TimeUtils.TIME_FORMAT_1));
//            }
//        }
    }

    private void writeToFile(String content,String name,String type){
        if(TextUtils.isEmpty(content)  || TextUtils.isEmpty(name)  || TextUtils.isEmpty(type) )
            return;


    }

    private void doGet(final String targetId,final ConversationType conversationType,String fromUserId,String datatime) {

//        IMNetWorkRequest.getMemberHistoryList(targetId, fromUserId, datatime, 10, page, new MySubscriber<IMHistoryBean>(IMHistoryBean.class) {
//            @Override
//            public void onBackError(String message) {
//                super.onBackError(message);
//            }
//
//            @Override
//            public void onBackNext(IMHistoryBean obj) {
//                super.onBackNext(obj);
//                if (obj == null)
//                    return;
//                List<IMHistoryBean.DataBean> dataBean = obj.getData();
//                if (dataBean == null)
//                    return;
//                List<Message> messageList = new ArrayList<>();
//                List<UIMessage> uiMessageList = new ArrayList<>();
//                for (IMHistoryBean.DataBean appContentBean : dataBean) {
//                    if (appContentBean == null)
//                        continue;
//                    String content = "";
//                    IMHistoryBean.DataBean.ContentBean contentBean = appContentBean.getContent();
//                    if (contentBean != null && !TextUtils.isEmpty(contentBean.getContent())) {
//                        content = appContentBean.getContent().getContent();
//                    }
//                    MessageContent messageContent = null;
//                    String type = appContentBean.getClassname();
//                    Log.v("ddd", "type : " + type);
//                    //文本类型
//                    if (!TextUtils.isEmpty(type)) {
//                        if (type.contains("TxtMsg")) {
//                            messageContent = new TextMessage(content);
//                        } else if (type.contains("LBSMsg")) {
//                            JSONObject jsonObject = new JSONObject();
////                            try {
////                                jsonObject.put("latitude",contentBean.get());
////                                jsonObject.put("longitude",contentBean.getContent());
////                                jsonObject.put("content","");
////                                jsonObject.put("extra","");
////                                jsonObject.put("user",contentBean.getUser());
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
//                            messageContent = new LocationMessage(jsonObject.toString().getBytes());
//                        } else if (type.contains("SRSMsg")) {
////                            messageContent = new SMESS
//                        } else if (type.contains("ImgMsg") && appContentBean.getContent() != null) {
//                            messageContent = new ImageMessage(appContentBean.getContent().getContent().getBytes());
//                            //语音
//                        } else if (type.contains("VcMsg")) {
////                            File file = new File(StoreAddressUtils.FELDSHER_VC_DIRECTORY + appContentBean.getMsgUID() + ".amr");
//                            File file = new File(StoreAddressUtils.FELDSHER_VC_DIRECTORY + appContentBean.getMsgUID() + ".wav");
//                            if (file.exists()) {
//                                file.delete();
//                            }
//                            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(contentBean.getContent().getBytes(), Base64.DEFAULT));
////                            ByteArrayInputStream bis = new ByteArrayInputStream(contentBean.getContent().getBytes());
//                            try {
//                                FileOutputStream fos = new FileOutputStream(file);
//                                int length = 0;
//                                byte[] buffer = new byte[1024];
//                                while ((length = bis.read(buffer)) != -1) {
//                                    fos.write(buffer, 0, length);
//                                }
//                                fos.flush();
//                                fos.close();
//                                bis.close();
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            messageContent = VoiceMessage.obtain(Uri.parse(file.getPath()), Integer.parseInt(contentBean.getDuration()));
//                            //文件类型
//                        } else if (type.contains("FileMsg") && contentBean != null) {
////                                messageContent =   FileMessage.obtain(Uri.parse(appContentBean.getContent().getFileUrl()));
////                            messageContent = new FileMessage()
//                            JSONObject jsonObject = new JSONObject();
//                            try {
//                                jsonObject.put("name", contentBean.getName());
//                                jsonObject.put("size", contentBean.getSize());
//                                jsonObject.put("type", contentBean.getType());
//                                jsonObject.put("localPath", "");
//                                jsonObject.put("fileUrl", contentBean.getFileUrl());
//                                jsonObject.put("extra", "");
//                                jsonObject.put("user", contentBean.getUser());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            messageContent = new FileMessage(jsonObject.toString().getBytes());
//                        }
//                    }
//                    if (messageContent == null)
//                        continue;
//                    IMHistoryBean.DataBean.ContentBean.UserBean userBean = contentBean.getUser();
//                    if (userBean != null) {
//                        UserInfo userInfo = new UserInfo(userBean.getId(), userBean.getName(), TextUtils.isEmpty(userBean.getPortrait()) ? null : Uri.parse(userBean.getPortrait()));
//                        messageContent.setUserInfo(userInfo);
//                    }
//                    Message message = Message.obtain(appContentBean.getTargetId(), ConversationType.setValue(Integer.parseInt(appContentBean.getTargetType())), messageContent);
//                    message.setUId(appContentBean.getMsgUID());
//                    message.setSenderUserId(appContentBean.getFromUserId());
//                    message.setMessageDirection(appContentBean.getDirection());
//                    ReceivedStatus receivedStatus = new ReceivedStatus(1);
//                    receivedStatus.setListened();
//                    message.setObjectName(type);
//                    message.setReceivedStatus(receivedStatus);
//                    message.setSentStatus(SentStatus.READ);
//
//                    if (!TextUtils.isEmpty(appContentBean.getDateTime())) {
//                        message.setSentTime(TimeUtils.getTimesForLong(appContentBean.getDateTime(), "yyyy/MM/dd HH:mm:ss"));
//                    }
//                    UIMessage uiMessage = UIMessage.obtain(message);
////
//                    messageList.add(message);
//                    uiMessageList.add(uiMessage);
//
//                }
//
//                List remoteList1 = ConversationFragment1.this.filterMessage(uiMessageList);
//                if (remoteList1 != null && remoteList1.size() > 0) {
//                    Iterator i$ = remoteList1.iterator();
//
//                    while (i$.hasNext()) {
//                        UIMessage uiMessage2 = (UIMessage) i$.next();
//                        ConversationFragment1.this.mListAdapter.add(uiMessage2, 0);
//                    }
//
//                    ConversationFragment1.this.mList.setTranscriptMode(0);
//                    ConversationFragment1.this.mListAdapter.notifyDataSetChanged();
//                    ConversationFragment1.this.mList.setSelection(messageList.size() + 1);
//                    ConversationFragment1.this.sendReadReceiptResponseIfNeeded(messageList);
//
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
////                e.getMessage()
//                super.onError(e);
//            }
//        });

    }

//    TextMessage,ImageMessage,
//    LocationMessage
    /**
     *
     * VoiceMessage,ImageMessage,GroupNotificationMessage,RichContentMessage,UnKnownMessage,HandshakeMessage,TextMessage,PublicServiceRichContentMessage,RealTimeLoscation
     *
     * CallSTerminateMessage,FileMessage,RecallNotificationMessage,PublicServiceMultiRichContentMessage,LocationMessage,InformationNotificationMessage,
     *
     * DiscussionNotificationMessage
     *
     * @return
     */
    private List<UIMessage> filterMessage(List<UIMessage> srcList) {
        Object destList;
        if(this.mListAdapter.getCount() > 0) {
            destList = new ArrayList();

            for(int i = 0; i < this.mListAdapter.getCount(); ++i) {
                Iterator i$ = srcList.iterator();
                while(i$.hasNext()) {
                    UIMessage msg = (UIMessage)i$.next();
                    if(!((List)destList).contains(msg) && msg.getMessageId() != ((UIMessage)this.mListAdapter.getItem(i)).getMessageId()) {
                        ((List)destList).add(msg);
                    }
                }
            }
        } else {
            destList = srcList;
        }

        return (List)destList;
    }

    private void sendReadReceiptAndSyncUnreadStatus(ConversationType conversationType, String targetId, long timeStamp) {
        if(conversationType == ConversationType.PRIVATE) {
            if(this.mReadRec && RongContext.getInstance().isReadReceiptConversationType(ConversationType.PRIVATE)) {
                RongIMClient.getInstance().sendReadReceiptMessage(conversationType, targetId, timeStamp);
            } else if(this.mSyncReadStatus) {
                RongIMClient.getInstance().syncConversationReadStatus(conversationType, targetId, timeStamp, (RongIMClient.OperationCallback)null);
            }
        } else if(conversationType.equals(ConversationType.GROUP) || conversationType.equals(ConversationType.DISCUSSION)) {
            RongIMClient.getInstance().syncConversationReadStatus(conversationType, targetId, timeStamp, (RongIMClient.OperationCallback)null);
        }

    }

    private void getLastMentionedMessageId(ConversationType conversationType, String targetId) {
        RongIMClient.getInstance().getUnreadMentionedMessages(conversationType, targetId, new ResultCallback<List<Message>>() {
            public void onSuccess(List<Message> messages) {
                if(messages != null && messages.size() > 0) {
                    ConversationFragment1.this.mLastMentionMsgId = ((Message)messages.get(0)).getMessageId();
                    int index = ConversationFragment1.this.mListAdapter.findPosition((long) ConversationFragment1.this.mLastMentionMsgId);
                    RLog.i("ConversationFragment1", "getLastMentionedMessageId " + ConversationFragment1.this.mLastMentionMsgId + " " + index);
                    if(ConversationFragment1.this.mLastMentionMsgId > 0 && index >= 0) {
                        ConversationFragment1.this.mList.smoothScrollToPosition(index);
                        ConversationFragment1.this.mLastMentionMsgId = 0;
                    }
                }

            }

            public void onError(ErrorCode e) {
            }
        });
    }

    private void sendReadReceiptResponseIfNeeded(List<Message> messages) {
        if(this.mReadRec && (this.mConversationType.equals(ConversationType.GROUP) || this.mConversationType.equals(ConversationType.DISCUSSION)) && RongContext.getInstance().isReadReceiptConversationType(this.mConversationType)) {
            ArrayList responseMessageList = new ArrayList();
            Iterator i$ = messages.iterator();

            while(i$.hasNext()) {
                Message message = (Message)i$.next();
                ReadReceiptInfo readReceiptInfo = message.getReadReceiptInfo();
                if(readReceiptInfo != null && readReceiptInfo.isReadReceiptMessage() && !readReceiptInfo.hasRespond()) {
                    responseMessageList.add(message);
                }
            }

            if(responseMessageList.size() > 0) {
                RongIMClient.getInstance().sendReadReceiptResponse(this.mConversationType, this.mTargetId, responseMessageList, (RongIMClient.OperationCallback)null);
            }
        }

    }
}

