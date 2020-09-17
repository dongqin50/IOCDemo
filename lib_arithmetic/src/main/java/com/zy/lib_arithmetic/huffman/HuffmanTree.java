package com.zy.lib_arithmetic.huffman;

import com.zy.lib_arithmetic.model.HuffmanDataModel;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 构建哈夫曼树
 */
public class HuffmanTree<T> {


    private LinkedList<Node<T>> mData;

    public HuffmanTree() {
        this.mData = new LinkedList<>();
    }

    private void generaNodeList(String str){

    }

    private void formatNodeList(List<HuffmanDataModel> datas){

        if(datas != null && !datas.isEmpty()){

            datas.forEach(new Consumer<HuffmanDataModel>() {
                @Override
                public void accept(HuffmanDataModel huffmanDataModel) {

                    HuffmanTree.Node node = new Node();


//                    mData.add()
                }
            });

        }
    }


    /**
     * 生成Huffman查询表
     */
    private void generaHuffmanTree(){

    }

    private Node createDirectionTopNode(Node left,Node right){
        Node top = new Node();
        top.value = -1;
        associateMultiplyNode(top,left,right);
        return top;
    }

    private void associateMultiplyNode(Node top,Node left,Node right){
        top.left = left;
        top.right = right;
        top.frequency = left.frequency + right.frequency;

        left.parent = top;
        left.command = 0;

        right.parent = top;
        right.command = 1;
    }

    private static class Node<T>{
        T value;
        int frequency;
        byte command;

        Node<T> left;
        Node<T> right;
        Node<T> parent;

    }
}
