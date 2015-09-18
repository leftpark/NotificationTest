package com.leftpark.android.notificationtest;

import java.util.Random;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static java.lang.Thread.sleep;


public class MainActivity extends Activity {
	private static final String TAG = "TEST_NOTI";
	
	private static final int HANDLE_EVENT_PROGRESS = 1;
	
	private static final int NOTIFICAITON_ID_PROGRESS = 100;
	
	private static final int PROGRESS_SLEEP_DEFAULT = 500;
	private static final int PROGRESS_MAX_DEFAULT = 50;
	
	private static final int REPEAT_SLEEP_DEFAULT = 1000;
	private static final int REPEAT_COUNT_DEFAULT = 10;
	
	private static final int BROADCAST_SLEEP_DEFAULT = 1000;

    
    private static int mProgressSleep = 1000;
    private static int mProgressMax = 100;
    
    private Handler mHandler;
    
    // NotificationManager
    private NotificationManager mNManager;
    
    // Builder for Progress
    private NotificationCompat.Builder mPBuilder;

    /***************************/
    
    // repeat test sleep time
    EditText mEText_repeat_sleep;
    
    // repeat test count
    EditText mEText_repeat_count;

    // Start repeat test Button
    Button mBtn_repeat_test;
    
    /***************************/
    
    // Sleep of progress bar
    EditText mEText_progress_sleep;
    
    // Maximum count of progress bar
    EditText mEText_progress_max;
    
    // progressed count
    TextView mTV_progress;
    
    // Start progress test Button
    Button mBtn_progress;
    
    /***************************/
    
    // Sleep of Broadcasting
    EditText mEText_bcast_sleep;
    
    // String broadcast test Button
    Button mBtn_bcast;
    
    /***************************/
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mHandler = new Handler() {
        	public void handleMessage(Message msg) {
        		super.handleMessage(msg);
        		switch(msg.what) {
        		case HANDLE_EVENT_PROGRESS:
        			int incr = msg.arg1;
        			mTV_progress.setText(incr+"/"+mProgressMax);
        			break;
        		default:
        			break;
        		}
        	}
        };
        
        /*****************************************************************/
        
        // sleep Time
        mEText_repeat_sleep = (EditText)findViewById(R.id.et_sleep);
        
        // count
        mEText_repeat_count = (EditText)findViewById(R.id.et_count);

        // start Button
        mBtn_repeat_test = (Button)findViewById(R.id.btn_noti1);
        mBtn_repeat_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	// sleep for progress bar
				int sleep = str2int(mEText_repeat_sleep.getText().toString());
				if (sleep <= 0)
					sleep = REPEAT_SLEEP_DEFAULT;
		        
		        // progress Maximum count
				int repeat = str2int(mEText_repeat_count.getText().toString());
				if (repeat <= 0)
					repeat = REPEAT_COUNT_DEFAULT;
				
            	test_repeat(sleep, repeat);
            }
        });
        
        /*****************************************************************/
        
        // sleep for progress bar
        mEText_progress_sleep = (EditText)findViewById(R.id.et_progress_sleep);
        
        // progress Maximum count
        mEText_progress_max = (EditText)findViewById(R.id.et_progress_max);
        
        // progressed count
        mTV_progress = (TextView)findViewById(R.id.tv_progress);
        
        // progress Button
        mBtn_progress = (Button)findViewById(R.id.btn_prograss);
        mBtn_progress.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// sleep for progress bar
				int sleep = str2int(mEText_progress_sleep.getText().toString());
				if (sleep <= 0)
					mProgressSleep = PROGRESS_SLEEP_DEFAULT;
				else
					mProgressSleep = sleep;
		        
		        // progress Maximum count
				int max = str2int(mEText_progress_max.getText().toString());
				if (max <= 0)
					mProgressMax = PROGRESS_MAX_DEFAULT;
				else
					mProgressMax = max;
				
				// start progressing
				test_progress();
			}
		});
        
        /*****************************************************************/
        
        mEText_bcast_sleep = (EditText)findViewById(R.id.et_bcast_sleep);
        
        Button mBtn_bcast;
     // progress Button
        mBtn_bcast = (Button)findViewById(R.id.btn_bcast);
        mBtn_bcast.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// sleep for progress bar
				int sleep = str2int(mEText_bcast_sleep.getText().toString());
				if (sleep <= 0)
					sleep = BROADCAST_SLEEP_DEFAULT;
				
				// start progressing
				test_broadcast(sleep);
			}
		});
        
        /*****************************************************************/
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void remove(int id) {
    	NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	m.cancel(id);
    }
    
    private void start_noti(int id) {
    	NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
    		.setSmallIcon(R.drawable.ic_launcher)
    		.setPriority(NotificationCompat.PRIORITY_MAX)
    		.setVibrate(new long[] {1, 1, 1})
    		.setCategory(NotificationCompat.CATEGORY_MESSAGE)
    		.setContentTitle("Heads-Up Notification")
    		.setContentText("ID = "+id);
    	
    	NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	m.notify(id, nb.build());
    	
    }
    
    private void test_repeat(int sleep, int repeat) {
    	for (int i=0; i < repeat; i++) {
    		start_noti(i);
    		// Sleeps the thread, simulating an operation that takes time
			try {
				// Sleep for x seconds
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    
    private void test_progress() {
    	
    	final int id = NOTIFICAITON_ID_PROGRESS;
    	
    	mNManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	mPBuilder = new NotificationCompat.Builder(this);
    	mPBuilder.setContentTitle("Progressing")
    	    .setContentText("Test Progressing")
    	    .setSmallIcon(R.drawable.ic_launcher)
	    	.setPriority(NotificationCompat.PRIORITY_MAX)
			.setVibrate(new long[] {1, 1, 1})
			.setCategory(NotificationCompat.CATEGORY_PROGRESS);
    	
    	new Thread(
			new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int incr;
					// Do the "lengthy" operation 20 times
					for (incr = 0; incr <= mProgressMax; incr+=1) {
						Message msg = new Message();
		        		msg.what = HANDLE_EVENT_PROGRESS;
		        		msg.arg1 = incr;
		        		mHandler.sendMessage(msg);
						// Sets the progress indicator to a max value, the
						// current completion percentage, and "determinate" state
						mPBuilder.setProgress(mProgressMax, incr, false)
							.setContentText(incr+"/"+mProgressMax);
						
						// Displays the progress bar for the first time.
						mNManager.notify(id, mPBuilder.build());
						
						// Sleeps the thread, simulating an operation that takes time
						try {
							// Sleep for x seconds
							Thread.sleep(mProgressSleep);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// When the loop is finished, updates the notification
					mPBuilder.setContentText("Complete")
					// Removes the progress bar
						.setProgress(0, 0, false);
					
					mNManager.notify(id, mPBuilder.build());
				}
				
			}).start();
    }
    
    private void test_broadcast(int sleep) {
    	Log.d(TAG,"broadcasting() : E");
    	Intent intent = new Intent();
    	intent.setAction("com.leftpark.android.HEADSUP");
    	sendBroadcast(intent);
    	
    	try {
    		Thread.sleep(sleep);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	Intent intent2 = new Intent();
    	intent2.setAction("com.leftpark.android.HEADSUP2");
    	sendBroadcast(intent2);
    	Log.d(TAG,"broadcasting() : X");
    }
    
    private int str2int(String number) {
    	if (number.length() == 0)
    		return 0;
    	
    	return Integer.parseInt(number);
    }
    
    private String int2str(int number) {
    	return Integer.toString(number);
    }

}
