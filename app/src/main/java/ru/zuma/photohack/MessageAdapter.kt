package ru.zuma.photohack

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val myDataset: ArrayList<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MessageViewHolder(frameLayout: FrameLayout) : RecyclerView.ViewHolder(frameLayout)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MessageAdapter.MessageViewHolder {
        // create a new view
        val frameLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false) as FrameLayout
        // set the view's size, margins, paddings and layout parameters
        return MessageViewHolder(frameLayout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.itemView.findViewById<TextView>(R.id.tv_message).text = myDataset[position].text
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}