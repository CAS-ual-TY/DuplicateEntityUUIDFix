package de.cas_ual_ty.deuf;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Mod(DEUF.MOD_ID)
public class DEUF
{
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final String MOD_ID = "deuf";
    
    private static final RandomSource RANDOM = RandomSource.create();
    
    public DEUF()
    {
        NeoForge.EVENT_BUS.addListener(this::fix);
    }
    
    public void fix(EntityJoinLevelEvent event)
    {
        if(event.getLevel() instanceof ServerLevel level)
        {
            Entity entity = event.getEntity();
            
            if(entity instanceof Player)
            {
                return;
            }
            
            UUID uuid = entity.getUUID();
            
            Entity existing = level.getEntity(uuid);
            
            if(existing != null && existing != entity)
            {
                UUID uuidNew = Mth.createInsecureUUID();
                while(level.getEntity(uuidNew) != null)
                {
                    uuidNew = Mth.createInsecureUUID(RANDOM);
                }
                
                entity.setUUID(uuidNew);
                
                DEUF.LOGGER.info("Changing UUID of entity {} that already existed from {} to {}", ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString(), uuid.toString(), uuidNew.toString());
            }
        }
    }
}