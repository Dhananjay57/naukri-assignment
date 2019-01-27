package yadav.dhananjay.presentation.ui.searchjob

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_git_job.view.*
import yadav.dhananjay.domain.model.GitJob
import yadav.dhananjay.presentation.R


class GitJobAdapter : ListAdapter<GitJob, RecyclerView.ViewHolder>(GitJobDiffer()) {
    fun setColorSpannable(myText: String): SpannableString {
        val spannableContent = SpannableString(myText)
        spannableContent.setSpan(ForegroundColorSpan(Color.RED), 0,  2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_git_job, parent, false)
        return GitJobViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GitJobViewHolder).run {

            holder?.tvTitle?.text = getItem(position).title
            holder?.tvLocation?.text = getItem(position).location
            holder?.tvSalary?.text = "\u20B9 20,000 - 30,000 per month"
        }
    }

    inner class GitJobViewHolder constructor(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle= view.tvTitle
        val tvLocation = view.tvLocation
        val tvSalary = view.tvSalary

    }
}

class GitJobDiffer : DiffUtil.ItemCallback<GitJob?>() {

    override fun areItemsTheSame(oldItem: GitJob, newItem: GitJob): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: GitJob, newItem: GitJob): Boolean {
        return oldItem == newItem
    }
}