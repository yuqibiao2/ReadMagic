package fuwit.com.ffreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 2017/1/10.
 */
public class EPCadapter extends BaseAdapter {

    private List<EPC> list ;
    private Context context ;

    public EPCadapter(Context context , List<EPC> list){
        this.context = context ;
        this.list = list ;
    }
    public int getCount() {
        return list.size() ;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_epc, null);
                holder.tvEpc = (TextView) convertView.findViewById(R.id.textView_epc);
                holder.tvId = (TextView) convertView.findViewById(R.id.textView_id);
                holder.tvCount = (TextView) convertView.findViewById(R.id.textView_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && !list.isEmpty()) {
                int id = position + 1;
                holder.tvId.setText("" + id);
                holder.tvEpc.setText(list.get(position).epc);
                holder.tvCount.setText("" + list.get(position).count);
            }
        }catch (Exception e){

        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvEpc ;
        TextView tvId ;
        TextView tvCount ;
    }
}
