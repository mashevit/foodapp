package com.example.android.myapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myapp.database.Dish;
import com.example.android.myapp.ingredi.AddIngredi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder> {
MyAdapterOnClickHandler mClickHandler;
Context mContext;
MyAdapterCb myAdapterCb;
   // SharedPreferences sharedPreferences;
int pos1=-1,pos2=-1,pos3=-1;
int mod=1;
    private ArrayList<Dish> mDish;

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//todo:if(viewType mod 2==0)




        int layoutId;

        switch (viewType) {

            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.emptydish;
                break;
            }

            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.my_holder;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new MyAdapterViewHolder(view);/*
        if(viewType==pos1){
            int layouId=R.layout.emptydish;


        }
        int layoutId=R.layout.my_holder;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new MyAdapterViewHolder(view);*/
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapterViewHolder holder, final int position) {
if(getItemViewType(position)==0){

    //holder.imageView.setImageResource(R.drawable. );
}
        else if(getItemViewType(position)!=0&&position>0){


       // int position=position1;
/*
        if (position + 1 == getItemCount()) {


            // set bottom margin to 72dp.
            setBottomMargin(holder.itemView, (int) (3 * Resources.getSystem().getDisplayMetrics().density));
        } else {*/
            // reset bottom margin back to zero. (your value may be different)
            ///setBottomMargin(holder.itemView, 0);
            holder.imageView2.setImageResource(R.drawable.ddfsgf);
        if(position>=(getItemCount()-9)){

            holder.mButton.setVisibility(View.GONE);
            holder.mCheckBox.setVisibility(View.GONE);
            holder.myTextView.setVisibility(View.GONE);

        }

        if(mDish!=null&& position<(getItemCount()-9)) {

            holder.mButton.setVisibility(View.VISIBLE);
            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.myTextView.setVisibility(View.VISIBLE);
           // int s = mDish == null ? 0 : mDish.size();
            //if (mDish == null && (position == pos1 || position == pos2 || position == pos3)) {
//todo: arrenge here
                holder.myTextView.setText(mDish.get(position).getDishname());
     /*        if (s != 0 && mDish != null && (position < s)) {
                int pos = position == 0 ? 0 : position - 1;
                if (pos != 0) {
                    Dish dish = mDish.get(position - 1);
                    String name = dish.getDishname();
                    holder.myTextView.setText(name);
                }
            }*/
    final Dish dish=mDish.get(position);

   final String id=mDish.get(position).getId();
            holder.mCheckBox.setOnCheckedChangeListener(null);
        holder.mCheckBox.setChecked(dish.isChecked());
        //sharedPreferences.getBoolean(id, false));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//todo:to improve
                            //SharedPreferences prefs = mContext.getSharedPreferences(mContext.getResources().getString(R.string.myprefs),Context.MODE_PRIVATE);
                          /*  SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(id, isChecked);// holder.mCheckBox.isChecked());
                            editor.commit();*/
                        //dish.setChecked(isChecked);
                            myAdapterCb.onCb(id,isChecked);
                holder.itemView.setSelected(isChecked);
                            ////todo: with for loop eventualy seperate icon to extract the checked boxes and their id of dish.
                };

        });

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddIngredi.class);
                Log.i("maaaasss",mDish.get(position).getId()+" " );

                intent.putExtra(AddIngredi.EXTRA_TASK_ID, mDish.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        }        else holder.myTextView.setText("general ");
    }  }





    public interface MyAdapterOnClickHandler {
        void onClick(String pos);//todo:unite 2 interfaces
    }

    public interface MyAdapterCb {
        void onCb(String pos,boolean bool);

    }

    public MyAdapter(@NonNull Context context, MyAdapterOnClickHandler clickHandler,MyAdapterCb myAdapterCb) {
        mContext = context;
        mClickHandler = clickHandler;
        this.myAdapterCb=myAdapterCb;
     //   sharedPreferences=  mContext.getSharedPreferences(mContext.getResources().getString(R.string.myprefs), mContext.MODE_PRIVATE);

      //  mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }




    @Override
    public int getItemCount() {
        if(mDish==null) return 9;
        return mDish.size()+9;
    }

    class MyAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView myTextView;
        final Button mButton;
        final CheckBox mCheckBox;

        final ImageView imageView;//=itemView.find

        final ImageView imageView2;//=itemView.find

        MyAdapterViewHolder(View itemView) {
            super(itemView);
            myTextView= itemView.findViewById(R.id.textView);
            mButton=itemView.findViewById(R.id.button);
            mCheckBox=itemView.findViewById(R.id.checkBox);

            imageView=itemView.findViewById(R.id.view);
           imageView2=itemView.findViewById(R.id.imgview2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
          int adapterPosition = getAdapterPosition();
           /*   if(mod==1) {pos1=adapterPosition;mod++;}
            else if (mod==2){pos2=adapterPosition;mod++;}
            else if (mod ==3){pos3=adapterPosition;mod=1;}*/

           String topass=(adapterPosition>0&&adapterPosition<(getItemCount()-9))?mDish.get(adapterPosition).getId():-1+"";
            mClickHandler.onClick(topass);
         //   itemView.setSelected(adapterPosition);

            //  notifyDataSetChanged();
        }
    }
    public ArrayList<Dish> getDishes() {
        mDish.remove(0); return mDish;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setDishes(List<Dish> Dishes) {
        ArrayList<Dish> ans=new ArrayList<Dish>();
        ans.addAll(Dishes);
        if(Dishes.size()>0)
        ans.add(0,Dishes.get(0));
        mDish = ans;
        notifyDataSetChanged();
    }

    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }

}



    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (position==0)
        return 0;//position % 2 * 2;
        return 1;
    }
}
