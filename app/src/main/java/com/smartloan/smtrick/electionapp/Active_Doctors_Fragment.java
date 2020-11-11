package com.smartloan.smtrick.electionapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Active_Doctors_Fragment extends Fragment {


    private LeedRepository leedRepository;
    ArrayList<Users> invoiceArrayList;
    ActiveUserAdapter invoiceAdapter;
    private RecyclerView userrecycler;

    public Active_Doctors_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);


        leedRepository = new LeedRepositoryImpl();
        invoiceArrayList = new ArrayList<>();
        userrecycler = (RecyclerView) view.findViewById(R.id.userRecycler);
        userrecycler.setHasFixedSize(true);
        userrecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        getActiveDoctors();


        return view;
    }


    private void getActiveDoctors() {

        leedRepository.readActiveUser("Active", new CallBack() {
            @Override
            public void onSuccess(Object object) {
                invoiceArrayList.clear();
                if (object != null) {
                    invoiceArrayList = (ArrayList<Users>) object;
                    serAdapter(invoiceArrayList);
                }
            }

            @Override
            public void onError(Object object) {
//                Utility.showMessage(getActivity(), getMessage(R.string.registration_fail));
            }
        });
    }

    private void serAdapter(ArrayList<Users> invoiceArrayList) {
        if (invoiceArrayList != null) {
            if (invoiceAdapter == null) {
                invoiceAdapter = new ActiveUserAdapter(getActivity(), invoiceArrayList);
                userrecycler.setAdapter(invoiceAdapter);
                invoiceAdapter.notifyDataSetChanged();
            } else {
                ArrayList<Users> arrayList = new ArrayList<>();
                arrayList.addAll(invoiceArrayList);
                invoiceAdapter.reload(arrayList);
                invoiceAdapter.notifyDataSetChanged();

            }
        }
    }



}
