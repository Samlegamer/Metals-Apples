package fr.samlegamer.metalapples.item;


import net.minecraft.world.effect.MobEffectInstance;

public record MAApple(String apple, int nutrition, float saturationMod, boolean alwaysEat, MobEffectInstance... effects) {
}