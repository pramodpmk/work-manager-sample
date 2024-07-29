package com.hifx.workmanagerexample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.hifx.workmanagerexample.ui.theme.WorkManagerExampleTheme
import com.hifx.workmanagerexample.work.SomeImportantWork
import com.hifx.workmanagerexample.work.SomeRecurringWork
import com.hifx.workmanagerexample.work.SomeWork
import java.time.Duration

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkManagerExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var oneTimeRequestState by remember {
                        mutableStateOf(0)
                    }
                    var periodicRequestState by remember {
                        mutableStateOf(0)
                    }
                    Column {
                        Spacer(modifier = Modifier.size(50.dp))
                        Text(
                            text = "Start On time work",
                            modifier = Modifier.clickable {
                                oneTimeRequestState = 1
                            },
                        )
                        Spacer(modifier = Modifier.size(50.dp))
                        Text(
                            text = "Start periodic work",
                            modifier = Modifier.clickable {
                                periodicRequestState = 1
                            },
                        )
                        Spacer(modifier = Modifier.size(50.dp))
                        Text(
                            text = "Stop periodic work",
                            modifier = Modifier.clickable {
                                periodicRequestState = 2
                            },
                        )
                        Spacer(modifier = Modifier.size(50.dp))
                        Text(
                            text = "Start important work",
                            modifier = Modifier.clickable {
                                oneTimeRequestState = 2
                            },
                        )
                    }
                    LaunchedEffect(key1 = oneTimeRequestState, block = {
                        if (oneTimeRequestState == 1) {
                            println("One time Work request created")
                            val constraints = Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                            val workRequest = OneTimeWorkRequestBuilder<SomeWork>()
                                .setConstraints(constraints)
                                .setInputData(
                                    inputData = Data.Builder()
                                        .putString("key-one", "data-one")
                                        .build(),
                                )
                                .build()
                            WorkManager.getInstance(applicationContext).beginWith(workRequest)
                                .enqueue()
                            println("One time Work enqueued")
                        } else if (oneTimeRequestState == 2) {
                            println("Important Work request created")
                            val constraints = Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                            val workRequest = OneTimeWorkRequestBuilder<SomeImportantWork>()
                                .setConstraints(constraints)
                                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                                .build()
                            WorkManager.getInstance(applicationContext)
                                .enqueue(workRequest)
                            println("Important Work enqueued")
                        }
                    })
                    LaunchedEffect(key1 = periodicRequestState, block = {
                        if (periodicRequestState == 2) {
                            WorkManager.getInstance(applicationContext).cancelAllWork()
                        } else if (periodicRequestState == 1) {
                            println("Periodic Work request created")
                            val constraints = Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                            val workRequest =
                                PeriodicWorkRequestBuilder<SomeRecurringWork>(Duration.ofMinutes(1))
                                    .setConstraints(constraints)
                                    .build()
                            WorkManager.getInstance(applicationContext).enqueue(workRequest)
                            println("Periodic Work enqueued")
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkManagerExampleTheme {
        Greeting("Android")
    }
}
