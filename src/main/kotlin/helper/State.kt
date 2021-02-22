package helper

import androidx.compose.runtime.*


@Composable
fun <T> rememberMutableStateOf(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember { mutableStateOf(value, policy) }

@Composable
fun <T> rememberMutableStateOf(
    vararg keys: Any?,
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
): MutableState<T> = remember(keys) { mutableStateOf(value, policy) }