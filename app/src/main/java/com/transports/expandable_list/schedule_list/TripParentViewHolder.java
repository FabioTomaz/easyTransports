package com.transports.expandable_list.schedule_list;

import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.transports.R;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class TripParentViewHolder extends GroupViewHolder {

    private TextView transports;
    private TextView hoursTotal;
    private TextView priceTotal;
    private TextView nTransports;
    private ImageView dropdownArrow;
    private Button buyBtn;

    public TripParentViewHolder(View itemView) {
        super(itemView);
        transports = (TextView) itemView.findViewById(R.id.transports_names);
        hoursTotal = (TextView) itemView.findViewById(R.id.hours_total);
        priceTotal = (TextView) itemView.findViewById(R.id.price_total);
        nTransports = (TextView) itemView.findViewById(R.id.n_transports);
        dropdownArrow = (ImageView) itemView.findViewById(R.id.dropdown_image);
        buyBtn = (Button) itemView.findViewById(R.id.buy);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("buyBtn", getAdapterPosition()+"");
                /*Intent intent = new Intent(v.getContext(), TicketUseActivity.class);
                intent.putExtra("", "");
                v.getContext().startActivity(intent);*/
            }
        });
    }

    public void setTripParent(TripParent tripTotal) {
        transports.setText(tripTotal.getTransports());
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