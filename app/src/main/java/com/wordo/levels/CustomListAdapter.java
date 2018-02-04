package com.wordo.levels;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.ScrollingMovementMethod;

import com.wordo.R;
import com.wordo.game.GameActivity;
import com.wordo.game.GameAllocatableUnit;

import java.util.List;

/**
 * Created by Arihant on 23-06-2016.
 */
public class CustomListAdapter extends BaseAdapter {

    private MediaPlayer mpClick;
    private LayoutInflater layoutInflater;
    private List<GameAllocatableUnit> listGameAllocatableUnit;
    private Context context;

    public CustomListAdapter(Context context, List<GameAllocatableUnit> listGameAllocatableUnit) {
        this.context = context;
        this.listGameAllocatableUnit = listGameAllocatableUnit;
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listGameAllocatableUnit.size();
    }

    @Override
    public Object getItem(int i) {
        return listGameAllocatableUnit.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addList(List<GameAllocatableUnit> listGameAllocatableUnit) {
        if (listGameAllocatableUnit != null)
            this.listGameAllocatableUnit.addAll(listGameAllocatableUnit);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = layoutInflater.inflate(R.layout.level_item, null);
            holder.btn = (Button) view.findViewById(R.id.btnLevel);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels - 40,
                    context.getResources().getDisplayMetrics().heightPixels / 3 - 20);
            params.setMargins(20, 10, 20, 10);
            holder.btn.setLayoutParams(params);
            view.setTag(holder);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
            animation.setDuration(500);
            view.startAnimation(animation);
        } else
            holder = (Holder) view.getTag();

        if (listGameAllocatableUnit != null) {
            final int LOCKED = 0, UNLOCKED_UNSOLVED = 1, UNLOCKED_SOLVED = 2;

            if (listGameAllocatableUnit.get(i).getLockType() == LOCKED) {
                holder.btn.setBackgroundResource(R.drawable.layers_select_levellock_button_bg);
                holder.btn.setText("  Level - " + listGameAllocatableUnit.get(i).getLevelNo());
            } else if (listGameAllocatableUnit.get(i).getLockType() == UNLOCKED_UNSOLVED) {
                holder.btn.setBackgroundResource(R.drawable.layers_select_levelunlock_unsolved_button_bg);
                holder.btn.setText("  Level - " + listGameAllocatableUnit.get(i).getLevelNo() + "\n  Score Required - " + listGameAllocatableUnit.get(i).getScoreNeeded());
            } else if (listGameAllocatableUnit.get(i).getLockType() == UNLOCKED_SOLVED) {
                holder.btn.setBackgroundResource(R.drawable.layers_select_levelunlock_solved_button_bg);
                holder.btn.setText("  Level - " + listGameAllocatableUnit.get(i).getLevelNo() + "\n  " + listGameAllocatableUnit.get(i).getWord());
            }

            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playClickMusic();
                    if (listGameAllocatableUnit.get(i).getLockType() == LOCKED)
                        Toast.makeText(context, "Game is locked. Complete unfinished levels.", Toast.LENGTH_SHORT).show();
                    else {

                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawableResource(R.color.color_white);
                        dialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.setContentView(R.layout.level_content_item);
                        dialog.getWindow().setLayout(context.getResources().getDisplayMetrics().widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);

                        ((TextView) dialog.findViewById(R.id.textDifficulty)).setText(listGameAllocatableUnit.get(i).getDifficultyLevel());
                        ((TextView) dialog.findViewById(R.id.textLevel)).setText("Level - " + listGameAllocatableUnit.get(i).getLevelNo());					
						((TextView) dialog.findViewById(R.id.textHint)).setMovementMethod(new ScrollingMovementMethod());
                        ((TextView) dialog.findViewById(R.id.textHint)).setText("Find a relevant wordo.\n\n" + listGameAllocatableUnit.get(i).getGloss());
                        ((TextView) dialog.findViewById(R.id.textScore)).setText("Score Required : \n" + listGameAllocatableUnit.get(i).getScoreNeeded());
                        ((TextView) dialog.findViewById(R.id.textTime)).setText("Time : \n" + listGameAllocatableUnit.get(i).getGameTime() + " sec");

                        Button button = (Button) dialog.findViewById(R.id.btnPlay);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, GameActivity.class);
                                intent.putExtra("word", listGameAllocatableUnit.get(i).getWord());
                                intent.putExtra("gloss", listGameAllocatableUnit.get(i).getGloss());
                                intent.putExtra("wordID", listGameAllocatableUnit.get(i).getWordID());
                                intent.putExtra("difficultyLevel", listGameAllocatableUnit.get(i).getDifficultyLevel());
                                intent.putExtra("levelNo", listGameAllocatableUnit.get(i).getLevelNo());
                                intent.putExtra("scoreNeeded", listGameAllocatableUnit.get(i).getScoreNeeded());
                                intent.putExtra("gameTime", listGameAllocatableUnit.get(i).getGameTime());
                                intent.putExtra("noOfPits", listGameAllocatableUnit.get(i).getNoOfPits());
                                intent.putExtra("noOfAngryBalls", listGameAllocatableUnit.get(i).getNoOfAngryBalls());
                                intent.putExtra("noOfMagnetBalls", listGameAllocatableUnit.get(i).getNoOfMagnetBalls());
                                intent.putExtra("lockType", listGameAllocatableUnit.get(i).getLockType());
                                dialog.dismiss();
                                context.startActivity(intent);
                                ((Activity) context).finish();
                                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });
                        dialog.show();
                    }
                }
            });
        }
        return view;
    }

    private void playClickMusic() {
        if (mpClick != null) {
            mpClick.stop();
            mpClick.release();
            mpClick = null;
        }
        mpClick = MediaPlayer.create(context, R.raw.buttonclick);
        mpClick.start();
    }

    public class Holder {
        Button btn;
    }

}


