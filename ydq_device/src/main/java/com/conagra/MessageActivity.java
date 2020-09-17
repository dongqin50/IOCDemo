package com.conagra;

//import io.rong.imlib.model.Conversation;

/**
 * Created by yedongqin on 2018/5/5.
 */

public class MessageActivity{}
//
//        extends DragBaseFragmentActivity<ActivityMessageBinding, MessagePresenter,MessageComponent> {
//
//    private Fragment mConversationListFragment;
////    private Conversation.ConversationType[] mConversationsTypes = null;
//    private List<Fragment> mFragment;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setLayoutView(R.layout.activity_message);
//        initializeInjector();
//        mFragment = new ArrayList<>();
//        initConversationList();
//        mFragment.add(mConversationListFragment);
//        mBinding.viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragment.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragment.size();
//            }
//        });
//    }
//
//    public void setMessageCount(int count){
////        mTvMessage.setVisibility(View.VISIBLE);
////        if(count > 99){
////            count = 99;
////        }else if(count <= 0){
////            mTvMessage.setVisibility(View.GONE);
////        }
////        mTvMessage.setText(count + "");
//    }
//
//    @Override
//    protected void initializeInjector() {
//        mComponent = DaggerMessageComponent.builder()
//                .applicationComponent(getApplicationComponent())
//                .activityModule(getActivityModule())
//                .messageModule(new MessageModule()).build();
//        mComponent.inject(this);
//    }
//
//    private Fragment initConversationList() {
//        if (mConversationListFragment == null) {
//            ConversationListFragment listFragment = new ConversationListFragment();
//            ConversationListAdapterEx conversationListAdapterEx =
//                    new ConversationListAdapterEx(RongContext.getInstance(), this, Arrays.asList(
//                    new MessageBean(1, "预约挂号", "预约挂号详细日期")), new BaseListener.Observable<MessageBean>() {
//                @Override
//                public void onAction(MessageBean messageBean) {
//                    Toast.makeText(MessageActivity.this,"点击",Toast.LENGTH_SHORT).show();
//                }
//            });
//            listFragment.setAdapter(conversationListAdapterEx);
//
////            if (isDebug) {
////                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
////                        .appendPath("conversationlist")
////                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
////                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
////                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
////                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
////                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
////                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
////                        .build();
////                mConversationsTypes = new Conversation.ConversationType[] {Conversation.ConversationType.PRIVATE,
////                        Conversation.ConversationType.GROUP,
////                        Conversation.ConversationType.PUBLIC_SERVICE,
////                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
////                        Conversation.ConversationType.SYSTEM,
////                        Conversation.ConversationType.DISCUSSION
////                };
////
////            } else {
//            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                    .appendPath("conversationlist")
//                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
//                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
//                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
//                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
//                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
//                    .build();
//            mConversationsTypes = new Conversation.ConversationType[] {Conversation.ConversationType.PRIVATE,
//                    Conversation.ConversationType.GROUP,
//                    Conversation.ConversationType.PUBLIC_SERVICE,
//                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
//                    Conversation.ConversationType.SYSTEM
//            };
////            }
//            listFragment.setUri(uri);
//            mConversationListFragment = listFragment;
//            return listFragment;
//        } else {
//            return mConversationListFragment;
//        }
//    }
//
//
//}
