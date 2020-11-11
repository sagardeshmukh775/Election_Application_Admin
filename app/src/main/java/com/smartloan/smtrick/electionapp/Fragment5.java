package com.smartloan.smtrick.electionapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;


public class Fragment5 extends Fragment {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    public Fragment5() {}

    Context context;
    Button btnhl,btnpl,btnml,btntp,btnbt,btndl;
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_fragment5, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Products");
        }
        btnhl = (Button) view.findViewById(R.id.btn_hl);
        btnpl = (Button) view.findViewById(R.id.btn_pl);
        btnml = (Button) view.findViewById(R.id.btn_ml);
        btntp = (Button) view.findViewById(R.id.btn_tp);
        btnbt = (Button) view.findViewById(R.id.btn_bt);
        btndl = (Button) view.findViewById(R.id.btn_dl);

        btnhl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
                mListener.changeFragement(new Fragment5());

                startActivity(new Intent(getActivity(), Recyclerfragment.class));

            }
        });

        btnpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
                mListener.changeFragement(new Fragment5());

                startActivity(new Intent(getActivity(), Recyclerfragment.class));

            }
        });

        btnml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
                mListener.changeFragement(new Fragment5());

                startActivity(new Intent(getActivity(), Recyclerfragment.class));

            }
        });

        btntp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
                mListener.changeFragement(new Fragment5());

                startActivity(new Intent(getActivity(), Recyclerfragment.class));

            }
        });

        btnbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
                mListener.changeFragement(new Fragment5());

                startActivity(new Intent(getActivity(), Recyclerfragment.class));

            }
        });

        btndl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Utility.showLongMessage(getActivity(), getString(R.string.lead_generated_success_message));
                mListener.changeFragement(new Fragment5());

                startActivity(new Intent(getActivity(), Recyclerfragment.class));

            }
        });







        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // NOTE: This is the part that usually gives you the error
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
