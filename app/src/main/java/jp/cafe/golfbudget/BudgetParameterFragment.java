package jp.cafe.golfbudget;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BudgetParameterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BudgetParameterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetParameterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetParameterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetParameterFragment newInstance(String param1, String param2) {
        BudgetParameterFragment fragment = new BudgetParameterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BudgetParameterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_budget_parameter, container, false);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCalcButtonClicked(View view) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }

        try {
            // 予算画面を表示
            this.moveToBudget(this.calcBudget());

        } catch (Exception ex){
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private Budget calcBudget(){

        // 予算を計算
        Budget budget = new BudgetCalculator().calculate(this.getBudgetParameter());

        return budget;
    }

    private BudgetParameter getBudgetParameter() {
        // 画面のパラメータを取得
        BudgetParameter budgetParameter = new BudgetParameter();

        String amount = ((EditText)this.getActivity().findViewById(R.id.budgetEditText)).getText().toString();
        budgetParameter.budgetTotalAmount = Integer.parseInt(amount);
        budgetParameter.golfers = Integer.parseInt(((EditText)this.getActivity().findViewById(R.id.golfersEditText)).getText().toString());
        budgetParameter.closest = Integer.parseInt(((EditText)this.getActivity().findViewById(R.id.closestEditText)).getText().toString());
        budgetParameter.longest = Integer.parseInt(((EditText) this.getActivity().findViewById(R.id.longestEditText)).getText().toString());

        budgetParameter.lowest = ((Switch)this.getActivity().findViewById(R.id.lowestSwitch)).isChecked();
        budgetParameter.booby = ((Switch)this.getActivity().findViewById(R.id.boobySwitch)).isChecked();
        budgetParameter.trophy = ((Switch)this.getActivity().findViewById(R.id.trophySwitch)).isChecked();
        budgetParameter.celemony = ((Switch)this.getActivity().findViewById(R.id.ceremonySwitch)).isChecked();

        return budgetParameter;
    }

    public void moveToBudget(Budget budget){
        ((MainActivity)this.getActivity()).moveToBudget(budget);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart(){
        super.onStart();

        //setCalcButtonListener();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        this.setCalcButtonListener();

    }

    private void setCalcButtonListener(){
        Button button = (Button) this.getActivity().findViewById(R.id.calc_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                onCalcButtonClicked(v);
            }
        });

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
