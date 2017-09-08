package com.example.pancho.w6.view.secondView;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.example.pancho.w6.R;
import com.example.pancho.w6.injection.secondview.DaggerSecondViewComponent;
import com.example.pancho.w6.model.Details.Details;
import com.example.pancho.w6.util.CONSTANTS;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondView extends Fragment implements SecondViewContract.View {

    private static final String TAG = "SecondView";

    Context context;

    @Inject
    SecondViewPresenter presenter;
    @BindView(R.id.img)
    BootstrapThumbnail img;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvGenre)
    TextView tvGenre;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvPage)
    TextView tvPage;

    private static final String ARG_PARAM1 = "param1";

    private String id;

    public static SecondView newInstance(String param1) {
        SecondView fragment = new SecondView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_second, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        ButterKnife.bind(this,view);

        setupDaggerComponent();
        presenter.attachView(this);
        presenter.setContext(context);

        presenter.makeRestCall(id, true);
    }

    private void setupDaggerComponent() {
        DaggerSecondViewComponent.create().insert(this);
    }

    @Override
    public void showError(String s) {

    }

    @Override
    public void sendResult(Details details) {
        tvTitle.setText(details.getTitle());
        if(details.getGenres()!=null && details.getGenres().size()>0)
            tvGenre.setText(details.getGenres().get(0).getName());
        tvPage.setText(details.getHomepage());
        tvLanguage.setText(details.getOriginalLanguage());
        Picasso.with(context).load(CONSTANTS.PATH_MOVIES_IMG + details.getPosterPath()).into(img);
    }

}
