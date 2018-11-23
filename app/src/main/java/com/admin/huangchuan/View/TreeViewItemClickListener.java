package com.admin.huangchuan.View;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.admin.huangchuan.adapter.TreeViewAdapter;
import com.admin.huangchuan.model.Element;

import java.util.ArrayList;

/**
 * TreeView item点击事件
 */
public class TreeViewItemClickListener implements OnItemClickListener {
    /**
     * adapter
     */
    private TreeViewAdapter treeViewAdapter;

    public TreeViewItemClickListener(TreeViewAdapter treeViewAdapter) {
        this.treeViewAdapter = treeViewAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //点击的item代表的元素
        Element element = (Element) treeViewAdapter.getItem(position);
        //树中的元素
        ArrayList<Element> elements = treeViewAdapter.getElements();
        //元素的数据源
        ArrayList<Element> elementsData = treeViewAdapter.getElementsData();

        //点击没有子项的item直接返回
//        if (!Boolean.parseBoolean(element.isHasChildren())) {



//            return;
//        }


    }

}
