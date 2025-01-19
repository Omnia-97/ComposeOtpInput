package com.example.composeotpinput.component


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composeotpinput.ui.theme.AppColors
import com.example.composeotpinput.ui.theme.Typography

@Composable
fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    focusedBorderColor: Color = AppColors.DarkBlue900,
    unfocusedBorderColor: Color = AppColors.Gray400,
    textStyle: TextStyle = Typography.titleLarge.copy(color = AppColors.DarkBlue900, textAlign = TextAlign.Center),
    width: Dp = 56.67.dp,
    isError: Boolean = false,
    errorBorderColor: Color = AppColors.Red,
    onFocusChanged: (FocusState) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Number,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Box(
        modifier = Modifier
            .width(width)
            .height(60.dp)
            .border(
                1.dp,
                color = when {
                    isError -> errorBorderColor
                    isFocused -> focusedBorderColor
                    else -> unfocusedBorderColor
                },
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxSize()
                .align(alignment = Alignment.Center)
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged(it) }
                .clip(RoundedCornerShape(8.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = AppColors.White,
                focusedContainerColor = AppColors.White,
                disabledContainerColor = AppColors.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Transparent,
            ),
            textStyle = textStyle,
            interactionSource = interactionSource,
        )
    }
}

@Preview
@Composable
fun OtpTextFieldPreview() {
    OtpTextField(
        value = "",
        onValueChange = {},
        focusRequester = FocusRequester()
    )
}