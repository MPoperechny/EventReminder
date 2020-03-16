package ru.mpoperechny.eventreminder.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import ru.mpoperechny.eventreminder.R
import ru.mpoperechny.eventreminder.databinding.ActivityAllEventsBinding

class AllEventsActivity : AppCompatActivity() {

    /*
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAllEventsBinding>(this, R.layout.activity_all_events).run {
            lifecycleOwner = this@AllEventsActivity
            viewModel = userViewModel
        }
    }
*/

    private val binding: ActivityAllEventsBinding by lazy {
        DataBindingUtil.setContentView<ActivityAllEventsBinding>(this, R.layout.activity_all_events)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.employee = "fdgd"

        supportActionBar?.title = getString(R.string.all_events_page_toolbar_title)

    }
}
