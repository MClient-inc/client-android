package ru.mclient.common.fab


open class ImmutableFab(
    override val state: FabState,
    private val onClick: () -> Unit,
) : Fab {

    override fun onClick() {
        onClick.invoke()
    }

}