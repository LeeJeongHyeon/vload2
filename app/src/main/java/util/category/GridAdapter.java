package util.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vmp.company.vexmall.R;

public class GridAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<CategoryVO> list;

    public GridAdapter(@NonNull Context context, int resource, ArrayList<CategoryVO> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater linf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = linf.inflate( resource, null );

        ImageView ca_icon = convertView.findViewById(R.id.ca_icon);
        TextView ca_name = convertView.findViewById(R.id.ca_name);

        if( !list.get(position).getCa_name().isEmpty() ) {
            ca_name.setVisibility(View.VISIBLE);
        }
        ca_name.setText( list.get(position).getCa_name() );
        convertView.setTag(list.get(position).getCa_id());
        ImageAsync task = new ImageAsync(context, list.get(position), ca_icon);
        task.execute();

        return convertView;
    }
}










