package eu.kanade.presentation.more.onboarding

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.kanade.tachiyomi.source.online.komga.Komga
import kotlinx.coroutines.flow.collectLatest
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import tachiyomi.presentation.core.components.material.padding
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

internal class LoginStep : OnboardingStep {

    private val keyPreference = "source_${Komga().id}";

    internal val preferences: SharedPreferences by lazy {
        Injekt.get<Application>().getSharedPreferences(keyPreference, 0x0000)
    }

    override val isComplete: Boolean = true

    @Composable
    override fun Content() {
        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var address by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false) }

        val context = LocalContext.current
        val handler = LocalUriHandler.current

        Column(
            modifier = Modifier.padding(25.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Mihonga Login",
                fontSize = 25.sp,
//                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = address,
                onValueChange = { newText ->
                    address = newText;
                    if (newText.toHttpUrlOrNull() != null && !newText.endsWith("/")){
                        preferences.edit().putString("Address", newText).apply()
                        isError = false
                    } else {
                        isError = true
                    }

                },
                label = { Text("Address") }
            )
            if (isError) {
                Text(
                    text = "The URL is invalid, malformed, or ends with a slash",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 2.dp),
                    textAlign = TextAlign.Start
                )
            }
            OutlinedTextField(
                value = username,
                onValueChange = { newText ->
                    username = newText;
                    preferences.edit().putString("Username", newText).apply()
                },
                label = { Text("Username") }
            )

            OutlinedTextField(
                value = password,
                onValueChange = { newPassword ->
                    password = newPassword;
                    preferences.edit().putString("Password", password).apply()
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Text(
                text = "You can always change this later in komga setting, or doing onboarding steps again through setting page",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 7.dp),
                textAlign = TextAlign.Justify
            )
        }
    }
}
