package com.chouzz.skyresourcereforge.entity;

import com.chouzz.skyresourcereforge.registration.ModEntities;
import com.chouzz.skyresourcereforge.registration.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class HeavySnowballEntity extends ThrowableItemProjectile {
    private static final int DAMAGE = 8;
    private static final float BLAZE_MULTIPLIER = 1.7f;

    public HeavySnowballEntity(EntityType<? extends HeavySnowballEntity> type, Level level) {
        super(type, level);
    }

    public HeavySnowballEntity(Level level, LivingEntity owner) {
        super(ModEntities.HEAVY_SNOWBALL.get(), owner, level);
    }

    public HeavySnowballEntity(Level level, double x, double y, double z) {
        super(ModEntities.HEAVY_SNOWBALL.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.HEAVY_SNOWBALL.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide && tickCount > 1) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            if (tickCount <= 1) return;
            if (result.getEntity() != null) {
                int damage = DAMAGE;
                if (result.getEntity() instanceof Blaze) {
                    damage = (int) (DAMAGE * BLAZE_MULTIPLIER);
                }
                result.getEntity().hurt(damageSources().thrown(this, getOwner()), damage);
            }
        }
    }
}
