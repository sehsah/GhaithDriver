package com.mahmoudsehsah.ghaithDriver.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.models.ChatList;
import com.mahmoudsehsah.ghaithDriver.models.MessageType;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mahmoud on 3/29/2018.
 */


public class MessagingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatList> messages;
    private Context context;

    /**
     * Constructor for Adapter
     *
     * @param messages list of messages will showed
     * @param context  context
     */
    public MessagingAdapter(List<ChatList> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         * check the type of view and return holder
         */
        if (viewType == MessageType.SENT_TEXT) {
            return new SentTextHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent_message_text, parent, false));
        } else if (viewType == MessageType.SENT_IMAGE) {
            return new SentImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent_message_img, parent, false));
        } else if (viewType == MessageType.RECEIVED_TEXT) {
            return new ReceivedTextHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received_message_text, parent, false));
        } else if (viewType == MessageType.RECEIVED_IMAGE) {
            return new ReceivedImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received_message_img, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder mHolder, int position) {

        int type = getItemViewType(position);
        ChatList message = messages.get(position);


        if (type == MessageType.SENT_TEXT) {
            SentTextHolder holder = (SentTextHolder) mHolder;
            holder.tvTime.setText(message.getTime());
            holder.tvMessageContent.setText(message.getMessage());

        } else if (type == MessageType.SENT_IMAGE) {
            SentImageHolder holder = (SentImageHolder) mHolder;
            holder.tvTime.setText(message.getTime());
            Picasso.with(context).load(message.getMessage()).into(holder.imgMsg);


        } else if (type == MessageType.RECEIVED_TEXT) {
            ReceivedTextHolder holder = (ReceivedTextHolder) mHolder;
            holder.tvTime.setText(message.getTime());
            holder.tvMessageContent.setText(message.getMessage());


        } else if (type == MessageType.RECEIVED_IMAGE) {
            ReceivedImageHolder holder = (ReceivedImageHolder) mHolder;
            holder.tvTime.setText(message.getTime());
            Picasso.with(context).load(message.getMessage()).into(holder.imgMsg);

        }


    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        /**
         * check the user id to detect if message sent or received
         * then check if message is text or img
         */
        SessionManager sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetails();
        int userID = Integer.parseInt(user.get(SessionManager.USER_ID));
        ChatList message = messages.get(position);
        int SendUser = message.getSend();
        Log.e("SendUser", String.valueOf(SendUser));
        Log.e("userID", String.valueOf(userID));
        if (userID == Integer.parseInt(String.valueOf(message.getSend()))) {

            if (message.getType().equals("1")) {
                return MessageType.SENT_TEXT;
            } else if (message.getType().equals("2")) {
                return MessageType.SENT_IMAGE;
            }

        } else {

            if (message.getType().equals("1")) {
                return MessageType.RECEIVED_TEXT;
            } else if (message.getType().equals("2")) {
                return MessageType.RECEIVED_IMAGE;
            }

        }

        return super.getItemViewType(position);
    }

    class SentMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;

        public SentMessageHolder(View itemView) {
            super(itemView);
            Typeface customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/jf.ttf");
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTime.setTypeface(customTypeOne);
            ButterKnife.bind(this, itemView);
        }
    }

    // sent message with type text
    class SentTextHolder extends SentMessageHolder {
        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;

        public SentTextHolder(View itemView) {
            super(itemView);
            Typeface customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/jf.ttf");
            tvMessageContent = itemView.findViewById(R.id.tv_message_content);
            ButterKnife.bind(this, itemView);
            tvMessageContent.setTypeface(customTypeOne);

        }
    }

    // sent message with type image
    class SentImageHolder extends SentMessageHolder {
        @BindView(R.id.img_msg)
        ImageView imgMsg;

        public SentImageHolder(View itemView) {
            super(itemView);
            imgMsg = itemView.findViewById(R.id.img_msg);
            ButterKnife.bind(this, itemView);

        }
    }

    // received message holders
    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;

        public ReceivedMessageHolder(View itemView) {
            super(itemView);
            Typeface customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/jf.ttf");
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTime.setTypeface(customTypeOne);
            ButterKnife.bind(this, itemView);
        }
    }

    // received message with type text
    class ReceivedTextHolder extends ReceivedMessageHolder {
        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;


        public ReceivedTextHolder(View itemView) {
            super(itemView);
            Typeface customTypeOne = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/jf.ttf");
            tvMessageContent = itemView.findViewById(R.id.tv_message_content);
            tvMessageContent.setTypeface(customTypeOne);
            ButterKnife.bind(this, itemView);
        }
    }

    // received message with type image

    class ReceivedImageHolder extends ReceivedMessageHolder {
        @BindView(R.id.img_msg)
        ImageView imgMsg;

        public ReceivedImageHolder(View itemView) {
            super(itemView);
            imgMsg = itemView.findViewById(R.id.img_msg);
            ButterKnife.bind(this, itemView);
        }
    }



}
