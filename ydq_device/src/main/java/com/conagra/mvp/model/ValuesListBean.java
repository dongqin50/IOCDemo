package com.conagra.mvp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedongqin on 16/10/9.
 *
 * 可以获取某一列的值,
 */
public class ValuesListBean<T,R> extends ArrayList<ValuesBean<T,R>> {

    private List<T> values1List;
    private List<R> values2List;

    public ValuesListBean(int initialCapacity) {
        super(initialCapacity);
        values1List = new ArrayList<>();
        values2List = new ArrayList<>();
    }

    public ValuesListBean() {
        values1List = new ArrayList<>();
        values2List = new ArrayList<>();
    }


    /**
     *
     * @param num 从0开始,某一列
     * @return
     */
    public List<T> getValues1(int num){

        if(num < 0 || num >= size()){
            return null;
        }

        for(ValuesBean<T,R> bean : this){
            values1List.add(bean.getValue1(num));
        }

        return values1List;
    }

//    /**
//     *
//     * @param num 从0开始,某一列
//     * @return
//     */
//    public String getValues1ToString(int num){
//
//        List<T> list = getValues1(num);
//        JSONArray jsonArray = new JSONArray();
//        StringBuilder builder = new StringBuilder();
//        builder.append("{");
//        if(list != null){
//            for (int i = 0; i < list.size(); i++){
//
//                builder.append("\"" + list.get(i) + "\"");
//                if(i < list.size() - 1){
//                    builder.append(",");
//                }
//
////                try {
////                    jsonArray.put(i,list.get(i));
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//            }
//        }
//        builder.append("}");
//
//        return builder.toString();
//    }

    public List<R> getValues2(int num){

        if(num < 0 || num >= size()){
            return null;
        }

        for(ValuesBean<T,R> bean : this){
            values2List.add(bean.getValue2(num));
        }

        return values2List;
    }



    @Override
    public void clear() {
        super.clear();
        values1List.clear();
        values2List.clear();
    }
}
