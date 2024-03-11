import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import presentation.app.App

@Composable fun MainView() = App()

lateinit var appContext: Context // Android specific context

// Android specific intent flow
@Suppress("ObjectPropertyName")
var _androidIntentFlow: MutableSharedFlow<Intent> = MutableSharedFlow()
val androidIntentFlow: SharedFlow<Intent> = _androidIntentFlow  // read-only shared flow received on Android side
