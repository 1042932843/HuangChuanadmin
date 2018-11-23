package com.admin.huangchuan.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.admin.huangchuan.R;
import com.admin.huangchuan.model.Element;

import java.util.ArrayList;

/**
 * TreeViewAdapter
 */
public class TreeViewAdapter extends BaseAdapter {
	/** 元素数据源 */
	private ArrayList<Element> elementsData;
	/** 树中元素 */
	private ArrayList<Element> elements;
	/** LayoutInflater */
	private LayoutInflater inflater;
	/** item的行首缩进基数 */
	private int indentionBase;
	private  int oo;

	public TreeViewAdapter(ArrayList<Element> elements, ArrayList<Element> elementsData, LayoutInflater inflater,int oo) {
		this.elements = elements;
		this.elementsData = elementsData;
		this.inflater = inflater;
		indentionBase = 80;
		this.oo=oo;
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public ArrayList<Element> getElementsData() {
		return elementsData;
	}

	@Override
	public int getCount() {
		return elements.size();
	}

	@Override
	public Object getItem(int position) {
		return elements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ResourceAsColor", "InlinedApi" }) @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.treeview_item, null);
			holder.disclosureImg = (ImageView) convertView.findViewById(R.id.disclosureImg);
			holder.contentText = (TextView) convertView.findViewById(R.id.contentText);
			holder.line = (LinearLayout) convertView.findViewById(R.id.line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Element element = elements.get(position);
		int level =Integer.parseInt(element.getLevel());
		holder.contentText.setPadding(
				indentionBase * (level - 1),
				holder.disclosureImg.getPaddingTop(),
				holder.disclosureImg.getPaddingRight(),
				holder.disclosureImg.getPaddingBottom());
		if (level == 1) {
//			convertView.setBackgroundColor(android.R.color.holo_blue_dark);
//			holder.contentText.setTextColor(android.R.color.black);
		}
		holder.contentText.setText(element.getContentText());

	 if (Boolean.parseBoolean(element.isHasChildren()) && !Boolean.parseBoolean(element.isExpanded())) {
			holder.disclosureImg.setImageResource(R.drawable.close);
			//这里要主动设置一下icon可见，因为convertView有可能是重用了"设置了不可见"的view，下同。
			holder.disclosureImg.setVisibility(View.VISIBLE);
		} else if (Boolean.parseBoolean(element.isHasChildren()) &&Boolean.parseBoolean(element.isExpanded())) {
			holder.disclosureImg.setImageResource(R.drawable.open);
			holder.disclosureImg.setVisibility(View.VISIBLE);
		} else if (!Boolean.parseBoolean(element.isHasChildren())) {
			holder.disclosureImg.setImageResource(R.drawable.close);
			holder.disclosureImg.setVisibility(View.INVISIBLE);
		}

		holder.line .setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (oo==1){
					if (element.isExpanded() == null) {
						element.setExpanded("true");
						//从数据源中提取子节点数据添加进树，注意这里只是添加了下一级子节点，为了简化逻辑
						int i = 1;//注意这里的计数器放在for外面才能保证计数有效
						for (Element e : elementsData) {
							if (null != e.getParendId() && !e.getParendId().equals("")) {
								String o = e.getParendId();
								String p = element.getId();
								if (o.equals(p)) {
									e.setExpanded("false");
									elements.add(position + i, e);
									i++;
								}
							}
						}
						notifyDataSetChanged();
					}
				}
				if (Boolean.parseBoolean(element.isExpanded())) {
					element.setExpanded("false");
					//删除节点内部对应子节点数据，包括子节点的子节点...
					ArrayList<Element> elementsToDel = new ArrayList<Element>();
					for (int i = position + 1; i < elements.size(); i++) {
						if (Integer.parseInt(element.getLevel()) >= Integer.parseInt(elements.get(i).getLevel()))
							break;
						elementsToDel.add(elements.get(i));
					}
					elements.removeAll(elementsToDel);
				notifyDataSetChanged();
				} else {
					element.setExpanded("true");
					//从数据源中提取子节点数据添加进树，注意这里只是添加了下一级子节点，为了简化逻辑
					int i = 1;//注意这里的计数器放在for外面才能保证计数有效
					for (Element e : elementsData) {
						if (null != e.getParendId() && !e.getParendId().equals("")) {
							String o = e.getParendId();
							String p = element.getId();
							if (o.equals(p)) {
								e.setExpanded("false");
								elements.add(position + i, e);
								i++;
							}
						}
					}
				notifyDataSetChanged();
				}


			}
		});

		return convertView;
	}

	static class ViewHolder{
		ImageView disclosureImg;
		TextView contentText;
		LinearLayout line;
	}
}
