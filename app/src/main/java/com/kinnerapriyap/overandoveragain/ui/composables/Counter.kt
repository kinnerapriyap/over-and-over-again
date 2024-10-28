package com.kinnerapriyap.overandoveragain.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kinnerapriyap.overandoveragain.R

@Composable
fun Counter(
    title: String,
    noOfAlarms: MutableIntState = remember { mutableIntStateOf(0) },
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp)
        )
        CounterButton(noOfAlarms = noOfAlarms)
    }
}

@Composable
private fun CounterButton(
    modifier: Modifier = Modifier,
    noOfAlarms: MutableIntState,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(16.dp)
            .border(0.5.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
    ) {
        IconButton(onClick = { noOfAlarms.intValue-- }) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = stringResource(R.string.decrease_count)
            )
        }
        Text(
            text = noOfAlarms.intValue.toString(),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = { noOfAlarms.intValue++ }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.increase_count)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterPreview() {
    Counter(title = "Counter")
}
