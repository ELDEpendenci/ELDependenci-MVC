package com.ericlam.mc.eldgui.component;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.CircuitIterator;
import com.ericlam.mc.eldgui.ELDGPlugin;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;

public final class AnimatedButton extends AbstractComponent implements Animatable {

    private final String[][] lores;
    private final Material[] icons;
    private final String[] displays;
    private final Integer[] numbers;
    private final int seconds;

    private BukkitTask task = null;

    public AnimatedButton(
            AttributeController attributeController,
            ItemStackService.ItemFactory itemFactory,
            String[][] lores,
            Material[] icons,
            String[] displays,
            Integer[] numbers,
            int seconds
    ) {
        super(attributeController, itemFactory);
        this.lores = lores;
        this.icons = icons;
        this.displays = displays;
        this.numbers = numbers;
        this.seconds = seconds;
    }

    @Override
    public void startAnimation() {
        if (seconds > 0 && this.task == null){
            this.task = new AnimatedRunnable().runTaskTimer(ELDGPlugin.getPlugin(ELDGPlugin.class), 20L, 20L);
        }
    }

    @Override
    public boolean isAnimating() {
        return task != null && !task.isCancelled();
    }

    @Override
    public void stopAnimation() {
        if (this.task == null || this.task.isCancelled()) return;
        this.task.cancel();
        this.task = null;
    }

    private class AnimatedRunnable extends BukkitRunnable {

        private long timer = 0;

        private final CircuitIterator<String> displayIterator = new CircuitIterator<>(displays);
        private final CircuitIterator<String[]> loreIterator = new CircuitIterator<>(lores);
        private final CircuitIterator<Material> iconIterator = new CircuitIterator<>(icons);
        private final CircuitIterator<Integer> numberIterator = new CircuitIterator<>(numbers);

        @Override
        public void run() {

            if (timer % seconds == 0){
                if (displayIterator.hasNext()){
                    itemFactory.display(displayIterator.next());
                }
                if (loreIterator.hasNext()){
                    itemFactory.lore(loreIterator.next());
                }
                if (iconIterator.hasNext()){
                    itemFactory.material(iconIterator.next());
                }
                if (numberIterator.hasNext()){
                    itemFactory.amount(numberIterator.next());
                }
                updateInventory();
            }

            timer++;
            if (timer == Long.MAX_VALUE){
                timer = 0;
            }
        }
    }

}
