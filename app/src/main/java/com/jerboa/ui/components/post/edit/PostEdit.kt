@file:OptIn(ExperimentalMaterial3Api::class)

package com.jerboa.ui.components.post.edit

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jerboa.R
import com.jerboa.db.Account
import com.jerboa.ui.components.common.DefaultBackButton
import com.jerboa.ui.components.common.MarkdownTextField
import com.jerboa.ui.components.common.PickImage
import com.jerboa.ui.theme.MEDIUM_PADDING
import com.jerboa.validatePostName
import com.jerboa.validateUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostHeader(
    navController: NavController = rememberNavController(),
    onEditPostClick: () -> Unit,
    formValid: Boolean,
    loading: Boolean,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.post_edit_edit_post),
            )
        },
        actions = {
            IconButton(
                enabled = formValid && !loading,
                onClick = onEditPostClick,
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                } else {
                    // Todo add are you sure cancel dialog
                    Icon(
                        Icons.Outlined.Save,
                        contentDescription = stringResource(R.string.form_submit),
                    )
                }
            }
        },
        navigationIcon = {
            // Todo add are you sure cancel dialog
            DefaultBackButton(navController)
        },
    )
}

@Composable
fun EditPostBody(
    name: String,
    onNameChange: (name: String) -> Unit,
    body: TextFieldValue,
    onBodyChange: (body: TextFieldValue) -> Unit,
    nsfw: Boolean,
    onNsfwChanged: ((Boolean) -> Unit)?,
    url: String,
    onUrlChange: (url: String) -> Unit,
    onPickedImage: (image: Uri) -> Unit,
    formValid: (valid: Boolean) -> Unit,
    account: Account?,
    modifier: Modifier = Modifier,
) {
    val nameField = validatePostName(name)
    val urlField = validateUrl(url)

    val scrollState = rememberScrollState()

    formValid(
        !nameField.hasError &&
            !urlField.hasError,
    )

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .imePadding()
            .padding(MEDIUM_PADDING)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MEDIUM_PADDING),
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            isError = nameField.hasError,
            label = {
                Text(text = nameField.label)
            },
            modifier = Modifier
                .fillMaxWidth(),
        )
        OutlinedTextField(
            label = {
                Text(text = urlField.label)
            },
            value = url,
            isError = urlField.hasError,
            onValueChange = onUrlChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier
                .fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("NSFW")
                Checkbox(
                    checked = nsfw,
                    onCheckedChange = onNsfwChanged
                )
            }
            PickImage(
                onPickedImage = onPickedImage,
            )
        }
        MarkdownTextField(
            text = body,
            onTextChange = onBodyChange,
            modifier = Modifier
                .fillMaxWidth(),
            outlined = true,
            account = account,
            focusImmediate = false,
            placeholder = stringResource(R.string.post_edit_body_placeholder),
        )
    }
}
@Preview
@Composable
fun EditPostBodyPreview() {
    EditPostBody(
        name = "",
        body = TextFieldValue(""),
        url = "",
        nsfw = false,
        onNsfwChanged = {},
        formValid = {},
        onBodyChange = {},
        onNameChange = {},
        onPickedImage = {},
        onUrlChange = {},
        account = null,
    )
}
