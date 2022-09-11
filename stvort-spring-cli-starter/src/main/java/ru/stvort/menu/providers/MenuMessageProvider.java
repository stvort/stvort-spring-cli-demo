package ru.stvort.menu.providers;

/**
 * Common interface for providers of standard menu messages such as prompt, greeting and so on
 * {@link ru.stvort.menu.providers.MenuMessageTypes}:
 *
 * @author Alxander Orudzhev
 *
 * @see ru.stvort.menu.providers.MenuMessageTypes
 */
public interface MenuMessageProvider {
    String getMessage();
    MenuMessageTypes getType();
}
