package com.chouzz.skyresourcereforge.network;

import com.chouzz.skyresourcereforge.SkyResourceReforge;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Modern NeoForge networking system for SkyResourceReforge
 *
 * This class handles packet registration and provides a channel for
 * client-to-server communication.
 */
public class ModNetwork {
    /**
     * Register all network packets with NeoForge's modern networking system.
     * Call this during FMLCommonSetupEvent.
     */
    public static void register() {
        // Packets are registered via CustomPacketPayload.Type
        // Server-bound packets are handled by IPayloadHandler
        // Registration is done in SkyResourceReforge.java
    }

    /**
     * Dump packet for clearing machine inventories
     * This handles the GUI dump button functionality for machines like
     * the Fusion Table and Condenser.
     *
     * Machine types:
     * 0 = Fusion Table
     * 1 = Condenser
     */
    public static record DumpPacket(BlockPos pos, int machineType) implements CustomPacketPayload {
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SkyResourceReforge.MODID, "dump");
        public static final CustomPacketPayload.Type<DumpPacket> TYPE = new CustomPacketPayload.Type<>(ID);

        public static final StreamCodec<FriendlyByteBuf, DumpPacket> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, DumpPacket::pos,
                ByteBufCodecs.VAR_INT, DumpPacket::machineType,
                DumpPacket::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public void handle(IPayloadContext context) {
            context.enqueueWork(() -> {
                // Server-side handling will be implemented when:
                // - Fusion Table block entity is implemented
                // - Condenser functionality is added to Casing
                // Machine types: 0=Fusion Table, 1=Condenser
            });
        }
    }
}
