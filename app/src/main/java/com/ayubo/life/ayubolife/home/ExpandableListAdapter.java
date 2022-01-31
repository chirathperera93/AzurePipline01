package com.ayubo.life.ayubolife.home;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.ImageListObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.webrtc.App;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	ImageLoader imageLoader;
	private Context _context;
	private List<DBString> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<ImageListObj>> _listDataChild;

	public ExpandableListAdapter(Context context, List<DBString> listDataHeader,
                                 HashMap<String, List<ImageListObj>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String imageLine=_listDataChild.get(_listDataHeader.get(groupPosition).getName()).get(childPosition).getImage();
        String childName=_listDataChild.get(_listDataHeader.get(groupPosition).getName()).get(childPosition).getName();
        System.out.println("=========Child============================="+childName);
	//	final String childText = (String) getChild(groupPosition, childPosition);
		final String childText ="Test";
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.relax_list_item_layout, null);
		}
		imageLine=ApiClient.BASE_URL+imageLine;

		ProgressBar progressNewsList = (ProgressBar) convertView.findViewById(R.id.progressNewsList);

		ImageView img_child_image = (ImageView) convertView.findViewById(R.id.img_child_image);
//		if (imageLoader == null)
//			imageLoader= App.getInstance().getImageLoader();
		String imageURL="https://www.google.lk/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png";

		System.out.println("=========image============================="+imageLine);

		//img_child_image.setImageUrl(imageLine, imageLoader);

	//	loadImage(imageLine,img_child_image,progressNewsList);


		return convertView;
	}
	private void loadImage(String imageUrl, final com.android.volley.toolbox.NetworkImageView imageView, final ProgressBar progressBar) {
		progressBar.setVisibility(View.VISIBLE);
		if (imageUrl != null) {
			imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

				@Override
				public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
					if (response.getBitmap() != null) {
						progressBar.setVisibility(View.GONE);
						imageView.setImageBitmap(response.getBitmap());
					}
				}

				@Override
				public void onErrorResponse(VolleyError error) {
				//	Log.e("onErrorResponse ", error.toString());
					progressBar.setVisibility(View.GONE);
				}
			});
		}
	}


	@Override
	public int getChildrenCount(int groupPosition) {

		int coun=_listDataChild.get(this._listDataHeader.get(groupPosition).getName()).size();
		System.out.println("=========getChildrenCount============================="+coun);
		return coun;

	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
		DBString headerTitle = (DBString) getGroup(groupPosition);
		String title,heading;
		heading=headerTitle.getName();
		title=headerTitle.getId();
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.relax_list_item_header_layout, null);
		}
           System.out.println("=========Header==============================="+headerTitle);
		TextView lblListHeader1 = (TextView) convertView.findViewById(R.id.lblListHeader1);
		TextView lblListHeader2 = (TextView) convertView.findViewById(R.id.lblListHeader2);
		//lblListHeader1.setTypeface(null, Typeface.BOLD);
		lblListHeader1.setText(title);
		lblListHeader2.setText(heading);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
