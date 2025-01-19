package com.example.composeotpinput.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composeotpinput.ui.theme.AppColors
import com.example.composeotpinput.ui.theme.Typography

/**
 * Overview:
 * `OtpInputs` is a custom composable for displaying and managing OTP (One-Time Password) input fields.
 * It divides the OTP input into separate fields for each digit, allowing smooth navigation between fields.
 *
 * Purpose:
 * This component is designed to handle OTP entry in a user-friendly manner, ensuring validation and automatic focus transition.
 *
 * Parameters:
 * - `modifier`: A [Modifier] for customizing the layout or style of the OTP view.
 * - `otpLength`: The total number of OTP digits (default is 6).
 * - `otpCode`: The current OTP value as a [String].
 * - `onOtpChange`: A callback triggered whenever the OTP value changes, passing the updated OTP as a [String].
 *
 * Features:
 * - Dynamically handles the number of digits based on `otpLength`.
 * - Automatically moves the focus to the next field when a digit is entered.
 * - Clears focus when the last field is filled.
 * - Handles digit deletion gracefully by moving focus back.
 *
 * Example:
 * ```
 * OtpInputs(
 *     otpCode = "123",
 *     onOtpChange = { newOtp ->
 *         println("New OTP: $newOtp")
 *     }
 * )
 * ```
 */
@Composable
fun OtpInputs(
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    focusedBorderColor: Color = AppColors.DarkBlue900,
    unfocusedEmptyBorderColor: Color = AppColors.Gray400,
    textStyle: TextStyle = Typography.titleLarge.copy(color = AppColors.DarkBlue900),
    width: Dp = 56.67.dp,
    isError: Boolean = false,
    errorBorderColor: Color = AppColors.Red,
    keyboardType: KeyboardType = KeyboardType.Number,
) {
    val focusRequesters = List(otpLength) { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val paddedOtpCode = otpCode.padEnd(otpLength, ' ')
    val otpCharList = paddedOtpCode.take(otpLength).toList()

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        otpCharList.forEachIndexed { index, char ->
            val isFocused = remember { mutableStateOf(false) }
            val borderColor = when {
                isError -> errorBorderColor
                isFocused.value -> focusedBorderColor
                char.isDigit() -> focusedBorderColor
                else -> unfocusedEmptyBorderColor
            }
            OtpTextField(
                value = char.takeIf { it.isDigit() }?.toString() ?: "",
                onValueChange = { value ->
                    if (value.isNotEmpty() && value[0].isDigit()) {
                        val newOtp = paddedOtpCode.toCharArray().apply {
                            this[index] = value[0]
                        }.concatToString()
                        onOtpChange(newOtp)

                        if (index < otpLength - 1 && value.length == 1) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (index == otpLength - 1 && newOtp.length == otpLength) {
                            focusManager.clearFocus()
                        }
                    } else if (value.isEmpty()) {
                        val newOtp = paddedOtpCode.toCharArray().apply {
                            this[index] = ' '
                        }.concatToString()
                        onOtpChange(newOtp)

                        if (index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                focusRequester = focusRequesters[index],
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                textStyle = textStyle,
                width = width,
                onFocusChanged = { focusState ->
                    isFocused.value = focusState.isFocused
                },
                keyboardType = keyboardType,
                isError = isError,
                errorBorderColor = errorBorderColor,
            )
        }
    }
}

@Preview
@Composable
fun OtpInputsPreview() {
    val otpLength = 6
    val otpCode = "123456"
    val isError = false
    OtpInputs(
        otpLength = otpLength,
        otpCode = otpCode,
        onOtpChange = { newOtp ->
            println("New OTP: $newOtp")
        },
        isError = isError,
    )
}