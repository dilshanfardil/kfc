package lk.sayuru.jungleapp.contact;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import lk.sayuru.jungleapp.R;
import lk.sayuru.jungleapp.db.entity.Contact;

public class ContactFragment extends Fragment {

    private ContactViewModel mViewModel;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        // TODO: Use the ViewModel
        final EditText fname=getView().findViewById(R.id.etxtFName);
        final EditText lname=getView().findViewById(R.id.etxtLName);
        final EditText relation=getView().findViewById(R.id.etxtRelation);
        final EditText phoneNumber=getView().findViewById(R.id.etxtPhoneNumber);
        Button submitButton=getView().findViewById(R.id.btnSubmitNewContact);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.contact = new Contact(
                        fname.getText().toString(),
                        lname.getText().toString(),
                        relation.getText().toString(),
                        phoneNumber.getText().toString()
                );

                mViewModel.insertNewContact();
                Navigation.findNavController(getView()).navigate(R.id.action_contact_to_contactList);
            }
        });

    }

}
