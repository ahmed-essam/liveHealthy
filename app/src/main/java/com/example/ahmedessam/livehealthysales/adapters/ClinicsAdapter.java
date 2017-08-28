package com.example.ahmedessam.livehealthysales.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.activities.EditClinicActivity;
import com.example.ahmedessam.livehealthysales.models.Clinic;
//import com.yackeen.livehealthy.doctor.R;
//import com.yackeen.livehealthy.doctor.dto.response.doctor.ClinicsItem;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abdelrhman Walid on 7/19/2017.
 */

public class ClinicsAdapter extends RecyclerView.Adapter<ClinicsAdapter.BaseClinicHolder> {


    static int selectedIndex = 0;
    private ArrayList<Clinic> clinics;
    private boolean isEnglish, showNamesOnly;
    private onClinicSelectedListener listener;
    int doctorId;
    boolean online;

    public ClinicsAdapter(ArrayList<Clinic> clinics, boolean showNamesOnly,int doctorId , boolean online) {
        this.clinics = clinics;
        this.online = online;
        if (Locale.getDefault().getDisplayLanguage()=="English"){
            isEnglish = true;
        }else{
            isEnglish = false;
        }
       this.doctorId = doctorId;
        this.showNamesOnly = showNamesOnly;
    }

    public void setListener(onClinicSelectedListener listener) {
        this.listener = listener;
    }

    public void setClinics(ArrayList<Clinic> clinics) {
        this.clinics = clinics;
        notifyDataSetChanged();
    }

    @Override
    public BaseClinicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = showNamesOnly ? R.layout.item_clinic_name : R.layout.item_clinic;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        if (showNamesOnly)
            return new NamesViewHolder(view);
        else
            return new EditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseClinicHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return clinics.size();
    }



    public interface onClinicSelectedListener {
        void onClinicSelected(int position);
    }

    class EditViewHolder extends BaseClinicHolder {
        @BindView(R.id.clinic_name)
        TextView clinicName;
        @BindView(R.id.editButton)
        Button editButton;

        EditViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        void bind(int position) {
            Clinic item = clinics.get(position);
            if (isEnglish)
                clinicName.setText(item.getClinicName());
            else
                clinicName.setText(item.getClinicNameAR());
            editButton.setVisibility(item.isEditable() ? View.VISIBLE : View.GONE);
            if (!online){
                editButton.setVisibility(View.GONE);
            }else{
                editButton.setVisibility(View.VISIBLE);
            }

        }

        @OnClick(R.id.editButton)
        void onEditClick() {
//            EditClinicActivity.start(itemView.getContext(), clinics, getLayoutPosition());
            EditClinicActivity.start(itemView.getContext(),clinics,getLayoutPosition(),(long)doctorId , false,online);
        }
    }

    class NamesViewHolder extends BaseClinicHolder {

        @BindView(R.id.name)
        TextView clinicName;

        public NamesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        void bind(int position) {
            Clinic item = clinics.get(position);
            if (isEnglish)
                clinicName.setText(item.getClinicName());
            else
                clinicName.setText(item.getClinicNameAR());

            clinicName.setSelected(selectedIndex == position);
        }

        @OnClick(R.id.name)
        void onNameClicked() {
            notifyItemChanged(selectedIndex);
            selectedIndex = getLayoutPosition();
            notifyItemChanged(selectedIndex);
            if (listener != null)
                listener.onClinicSelected(selectedIndex);
        }
    }

    abstract class BaseClinicHolder extends RecyclerView.ViewHolder {

        public BaseClinicHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(int position);
    }
}
