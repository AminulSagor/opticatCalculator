package com.somadhan.fiberlasercalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.MobileAds
import androidx.compose.material3.AlertDialog


class MainActivity : ComponentActivity() {
    private lateinit var interstitialAdView: InterstitialAdView
    private var clickCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        interstitialAdView = InterstitialAdView(this)
        interstitialAdView.loadInterstitialAd()
        setContent {
            OpticalCalcApp(
                onCalculateClick = { handleCalculateClick() }
            )
        }
    }
    private fun handleCalculateClick() {
        clickCount++
        if (clickCount > 1) {
            if (interstitialAdView.isAdLoaded()) {
                interstitialAdView.showInterstitialAd(this)
            }
            // Reset click count after showing the ad
            clickCount = 0
        }
    }
}


@Composable
fun OpticalCalcApp(onCalculateClick: () -> Unit) {
    val viewModel: CalculatorViewModel = viewModel()
    val splitType by viewModel.splitType.collectAsState()
    val inputPower by viewModel.inputPower.collectAsState()
    val splitRatio by viewModel.splitRatio.collectAsState()
    val splitPercentage by viewModel.splitPercentage.collectAsState()
    val result by viewModel.result.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4)),
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .wrapContentSize(Alignment.TopStart)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(128.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Fiber Laser Calculator",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = inputPower,
                        onValueChange = { viewModel.setInputPower(it) },
                        label = { Text("Input Power (dBm)") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Select Split Type:")

                    Spacer(modifier = Modifier.height(8.dp))

                    // Call CustomDropdownMenu here
                    CustomDropdownMenu(
                        splitType = splitType,
                        onSplitTypeChange = { newType -> viewModel.setSplitType(newType) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dynamic input fields based on split type
                    if (splitType == "ratio") {
                        OutlinedTextField(
                            value = splitRatio,
                            onValueChange = { viewModel.setSplitRatio(it) },
                            label = { Text("Enter Split Ratio (e.g., 4 for 1:4)") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        OutlinedTextField(
                            value = splitPercentage,
                            onValueChange = { viewModel.setSplitPercentage(it) },
                            label = { Text("Enter Split Percentage (e.g., 70)") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.calculatePower()
                            showDialog.value = true
                            onCalculateClick()},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745))
                    ) {
                        Text(text = "Calculate")
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    if (showDialog.value) {
                        AlertDialog(
                            onDismissRequest = { showDialog.value = false },
                            title = { Text(text = "Calculation Result") },
                            text = { Text(text = result) },
                            confirmButton = {
                                Button(onClick = { showDialog.value = false }) {
                                    Text("OK")
                                }
                            }
                        )
                    }

                    val annotatedString = buildAnnotatedString {
                        pushStringAnnotation(tag = "URL", annotation = "https://www.facebook.com/profile.php?id=100007919205769L")
                        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                            append("Click to visit developer fb account")
                        }
                        pop()
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        val uriHandler = LocalUriHandler.current
                        BasicText(
                            text = annotatedString,
                            modifier = Modifier.clickable {
                                annotatedString.getStringAnnotations("URL", 0, annotatedString.length)
                                    .firstOrNull()?.let { annotation ->
                                        uriHandler.openUri(annotation.item)
                                    }
                            }
                        )
                    }

                    BannerAdView()
                }
            }

        }
    )
}


