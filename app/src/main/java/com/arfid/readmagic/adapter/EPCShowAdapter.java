package com.arfid.readmagic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arfid.readmagic.R;
import com.arfid.readmagic.bean.EPCBean;

import java.util.List;


public class EPCShowAdapter extends BaseAdapter {

    private List<EPCBean> list;
    private Context context;

    public EPCShowAdapter(Context context, List<EPCBean> list) {
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_epc, null);
                holder.tvEpc = (TextView) convertView.findViewById(R.id.tv_epc);
                holder.tvId = (TextView) convertView.findViewById(R.id.tv_id);
                holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && !list.isEmpty()) {
                int id = position + 1;
                holder.tvId.setText("" + id);
                holder.tvEpc.setText(list.get(position).getEpc());
                holder.tvCount.setText("" + list.get(position).getCount());
            }
        } catch (Exception e) {

        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvEpc;
        TextView tvId;
        TextView tvCount;
    }
}
