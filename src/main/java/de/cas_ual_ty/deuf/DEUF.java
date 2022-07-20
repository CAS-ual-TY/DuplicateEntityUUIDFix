package de.cas_ual_ty.deuf;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.UUID;

@Mod(DEUF.MOD_ID)
public class DEUF
{
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final String MOD_ID = "deuf";
    
    private static final Random RANDOM = new Random();
    
    public DEUF()
    {
        MinecraftForge.EVENT_BUS.addListener(this::fix);
    }
    
    public void fix(EntityJoinWorldEvent event)
    {
        if(event.getWorld() instanceof ServerLevel level)
        {
            Entity entity = event.getEntity();
            UUID uuid = entity.getUUID();
            
            if(level.getEntity(uuid) != entity)
            {
                UUID uuidNew = Mth.createInsecureUUID(RANDOM);
                while(level.getEntity(uuidNew) != null)
                {
                    uuidNew = Mth.createInsecureUUID(RANDOM);
                }
                
                entity.setUUID(uuidNew);
                
                DEUF.LOGGER.info("Changing UUID of entity {} that already existed from {} to {}", entity.getType().getRegistryName().toString(), uuid.toString(), uuidNew.toString());
            }
        }
    }
}