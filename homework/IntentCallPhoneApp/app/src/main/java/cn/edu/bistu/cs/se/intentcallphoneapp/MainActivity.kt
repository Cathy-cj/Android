package cn.edu.bistu.cs.se.intentcallphoneapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cn.edu.bistu.cs.se.intentcallphoneapp.ui.theme.IntentCallPhoneAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntentCallPhoneAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val mContext = LocalContext.current
    val singlePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()) {}
    val permission = Manifest.permission.CALL_PHONE
    Button(onClick = {
        if (mContext.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            singlePermissionResultLauncher.launch(permission)
        } else {
            val phoneNumber="13800138000"
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }

            if (intent.resolveActivity(mContext.packageManager) != null) {
                mContext.startActivity(intent)
            }
        }
    }) {
        Text(
            text = "Call",
            modifier = modifier
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IntentCallPhoneAppTheme {
        Greeting("Android")
    }
}