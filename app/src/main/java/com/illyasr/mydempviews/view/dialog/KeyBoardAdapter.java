package com.illyasr.mydempviews.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.ItemKeyBinding;

import java.util.Arrays;
import java.util.List;

/**
 * TODO
 *
 * @author qingshilin
 * @version 1.0
 * @date 2022/4/25 10:45
 */
public class KeyBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> list = Arrays.asList("1","2","3","4","5","6","7","8","9",".","0");
    private StringBuilder sendStr = new StringBuilder();
    private int maxLength = 10;//最大长度
    private int pointEnd = 2;//小数点之后保留几位



    public KeyBoardAdapter setOnKeyBoardInterface(OnKeyBoardInterface onKeyBoardInterface) {
        this.onKeyBoardInterface = onKeyBoardInterface;
        return this;
    }

    private OnKeyBoardInterface onKeyBoardInterface;
    public interface OnKeyBoardInterface {
        void OnNumber(String number);
        void OnEnd(String result);
    }

    public KeyBoardAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KeyVH(LayoutInflater.from(context).inflate(R.layout.item_key,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        KeyVH holder = (KeyVH) viewHolder;
        holder.binding.card.setCardBackgroundColor(position<=list.size()-1
                ?context.getResources().getColor(R.color.white):context.getResources().getColor(R.color.transparent));
        holder.binding.tvNum.setText(position<=list.size()-1?
                list.get(position):"");
        holder.binding.imgDel.setVisibility(position == getItemCount() - 1?View.VISIBLE:View.GONE);
        holder.binding.card.setOnClickListener(v -> {
            if (onKeyBoardInterface == null) {
                return;
            }
            switch (holder.binding.tvNum.getText().toString()) {
                case "0":
                    // 这里只添加可以添加0的场景
                    /**
                     * 可以输入0的场景只有
                     * 1.为空的时候
                     * 2,已经输入的数字不等于0(单纯的0)
                     * 3.前面有小数点的存在,那这个0可以一直按下去(理论上兼并到前面那个里面去了)
                     */
                    onKeyBoardInterface.OnNumber("0");
                    if (sendStr.length()>=maxLength){
                        return;
                    }
                    if (sendStr.toString().contains(".")&&sendStr.toString().substring(sendStr.toString().indexOf(".")).length()>=3){
                        return;
                    }
                    if (sendStr.length()==0|| !sendStr.toString().equals("0")){
                        sendStr.append("0");
                    }
                    onKeyBoardInterface.OnEnd(sendStr.toString());
                    break;
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    onKeyBoardInterface.OnNumber(holder.binding.tvNum.getText().toString());
                    if (sendStr.length()>=maxLength){
                        return;
                    }
                    if (sendStr.toString().contains(".")&&sendStr.toString().substring(sendStr.toString().indexOf(".")).length()>=3){
                        return;
                    }
                    if (sendStr.toString().equals("0")){
                        sendStr = new StringBuilder();
                    }
                    sendStr.append(holder.binding.tvNum.getText().toString());
                    onKeyBoardInterface.OnEnd(sendStr.toString());
                    break;
                case ".":
                    if (sendStr.length()>=maxLength){
                        return;
                    }
                    if (!sendStr.toString().contains(".")){
                        if (sendStr.length()==0){
                            sendStr.append("0.");
                        }else {
                            sendStr.append(".");
                        }
                    }
                    onKeyBoardInterface.OnEnd(sendStr.toString());

                    break;
                case "":
                    if (sendStr.length()>0){
                        sendStr.deleteCharAt(sendStr.length() - 1);
                    }
                    onKeyBoardInterface.OnEnd(sendStr.toString());
                    break;
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    class KeyVH extends RecyclerView.ViewHolder {
        ItemKeyBinding binding;
        public KeyVH(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
