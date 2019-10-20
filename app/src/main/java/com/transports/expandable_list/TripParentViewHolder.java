package com.transports.expandable_list;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.transports.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class TripParentViewHolder extends GroupViewHolder {

    private TextView hoursTotal;
    private TextView priceTotal;
    private TextView nTransports;
    private ImageView dropdownArrow;

    public TripParentViewHolder(View itemView) {
        super(itemView);
        hoursTotal = (TextView) itemView.findViewById(R.id.hours_total);
        priceTotal = (TextView) itemView.findViewById(R.id.price_total);
        nTransports = (TextView) itemView.findViewById(R.id.n_transports);
        dropdownArrow = (ImageView) itemView.findViewById(R.id.dropdown_image);
    }

    public void setTripParent(TripParent tripTotal) {
        hoursTotal.setText(tripTotal.getDepartureHour() + " - "+tripTotal.getArrivalHour());
        priceTotal.setText(tripTotal.getTotalPrice()+"€");
        nTransports.setText(tripTotal.getTotalTransports()+"");
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        dropdownArrow.setAnimation(rotate);
        dropdownArrow.startAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        dropdownArrow.setAnimation(rotate);
        dropdownArrow.startAnimation(rotate);
    }
}