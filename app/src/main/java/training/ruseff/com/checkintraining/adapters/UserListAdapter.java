package training.ruseff.com.checkintraining.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import training.ruseff.com.checkintraining.R;
import training.ruseff.com.checkintraining.models.User;

public class UserListAdapter extends ArrayAdapter<User> implements Filterable {

    private int lastPosition = -1;

    private ArrayList<User> originalData;
    private ArrayList<User> filteredData;
    Context mContext;
    String colorToCheck;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();

    private static class ViewHolder {
        TextView nameTextView;
    }

    public UserListAdapter(ArrayList<User> data, Context context, String colorToCheck) {
        super(context, android.R.layout.simple_list_item_1, data);
        this.originalData = data;
        this.filteredData = data;
        this.mContext = context;
        this.colorToCheck = colorToCheck;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public User getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void remove(User item) {
        filteredData.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User dataModel = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder.nameTextView = convertView.findViewById(android.R.id.text1);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        viewHolder.nameTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        viewHolder.nameTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        lastPosition = position;
        viewHolder.nameTextView.setText(dataModel.getName());
        if (dataModel.isChecked()) {
            viewHolder.nameTextView.setBackgroundColor(Color.parseColor(colorToCheck));
            viewHolder.nameTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.check, 0);
        }
        return convertView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<User> list = originalData;
            int count = list.size();
            final ArrayList<User> nlist = new ArrayList<>(count);
            User filterableUser;
            for (int i = 0; i < count; i++) {
                filterableUser = list.get(i);
                if (filterableUser.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableUser);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }

    }
}
