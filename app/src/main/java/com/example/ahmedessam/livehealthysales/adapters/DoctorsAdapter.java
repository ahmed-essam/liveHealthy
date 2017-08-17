package com.example.ahmedessam.livehealthysales.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ahmedessam.livehealthysales.R;
import com.example.ahmedessam.livehealthysales.activities.DoctorDetailActivity;
import com.example.ahmedessam.livehealthysales.models.Doctor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmed essam on 04/08/2017.
 */

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.doctor_image)
        ImageView doctorImage;
        @BindView(R.id.doctor_name)
        TextView doctorName;
        @BindView(R.id.speciality) TextView doctorSpeciality;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void bindView(Doctor doctor){

            Glide.with(getContext())
                    .load(doctor.getImage())
                    .placeholder(R.drawable.doctor_icon)
                    .error(R.drawable.doctor_icon)
                    .into(doctorImage);
            doctorName.setText(doctor.getName());
            doctorSpeciality.setText(doctor.getSpeciality());

        }

        @Override
        public void onClick(View view) {
            Intent intent = DoctorDetailActivity.newDoctorDetail(getContext(),
                    doctors.get(getPosition()).getDoctorID(),
                    doctors.get(getPosition()).isDeleted());
            getContext().startActivity(intent);
        }
    }

    private List<Doctor> doctors;
    private Context context;

    public DoctorsAdapter(Context context) {
        this.context = context;
        doctors = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bookView = inflater.inflate(R.layout.doctors_item_layout, parent, false);

        DoctorsAdapter.ViewHolder viewHolder = new DoctorsAdapter.ViewHolder(bookView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.bindView(doctor);

    }

    public void addAll(List<Doctor> doctorList){
        this.doctors.clear();
        this.doctors.addAll(doctorList);
        notifyDataSetChanged();
    }

    public void addItems(List<Doctor> doctorList){
        this.doctors.addAll(doctorList);
        notifyDataSetChanged();
    }
    public void clearAdapter(){
        this.doctors.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.doctors.size();
    }

}
