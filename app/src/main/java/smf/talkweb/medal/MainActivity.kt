package smf.talkweb.medal

import ando.file.core.FileOperator
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.core.context.KoinContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import smf.talkweb.medal.ui.theme.MedalTheme
import smf.talkweb.medal.ui.viewModel.AccountViewModel
import smf.talkweb.medal.ui.viewModel.MedalAppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedalTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

class MedalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FileOperator.init(this, BuildConfig.DEBUG)
        startKoin {
            androidLogger()
            androidContext(this@MedalApplication)
            modules(
                module {
                    single { MedalAppViewModel() }
                    single { AccountViewModel().apply { init(this@MedalApplication) } }
                }
            )
        }
    }
}