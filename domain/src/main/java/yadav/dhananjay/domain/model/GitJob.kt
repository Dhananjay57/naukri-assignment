package yadav.dhananjay.domain.model

import android.text.Html
import android.text.Spanned
import kotlin.random.Random


data class GitJob(
        val id: String,
        var type: String,
        val url: String,
        val created_at: String,
        val company: String,
        val company_url: String,
        val location: String,
        val title: String,
        val description: String,
        val company_logo: String
) {
    fun getHtmlDescription(): Spanned {
        return Html.fromHtml(description)
    }

    companion object {


        val JOB_TYPE_SALES = "Sales"
        val JOB_TYPE_DESIGN = "Design"
        val JOB_TYPE_ENGINEERING = "Product"
        val JOB_TYPES = mutableListOf(JOB_TYPE_SALES, JOB_TYPE_DESIGN, JOB_TYPE_ENGINEERING)

        fun getRandomJobType() = JOB_TYPES[Random.nextInt(0, JOB_TYPES.size)]
    }
}



