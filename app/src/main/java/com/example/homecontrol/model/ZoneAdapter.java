package com.example.homecontrol.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homecontrol.R;

public class ZoneAdapter extends BaseAdapter {

	private Context context;
	private ZoneList list;
	
	public ZoneAdapter(Context c, ZoneList l){
		this.context = c;
		this.list = l;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null){
			// inflate layout
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.zone_item_list, parent, false);
			
			// set up viewholder
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.subTitle = (TextView) convertView.findViewById(R.id.tvSubTitle);
			holder.icon = (ImageView) convertView.findViewById(R.id.ivIcon);
			
			// store holder with the view
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(list.get(position).getName());
		holder.subTitle.setText("" + list.get(position).getImgResId() + "");
		holder.icon.setImageResource(list.get(position).getImgResId());
		return convertView;
	}
	
	private class ViewHolder {
		TextView title;
		TextView subTitle;
		ImageView icon;
		
	}

}
