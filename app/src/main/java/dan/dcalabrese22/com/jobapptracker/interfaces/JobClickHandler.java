package dan.dcalabrese22.com.jobapptracker.interfaces;

import android.view.View;

import dan.dcalabrese22.com.jobapptracker.objects.Job;

/**
 * Created by dcalabrese on 12/6/2017.
 */

public interface JobClickHandler {

    void onJobClick(Job job);
    void onJobDescriptionClick(View view, Job job);

}
