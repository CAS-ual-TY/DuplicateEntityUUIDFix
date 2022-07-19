package de.cas_ual_ty.deuf;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

@Mod(DEUF.MOD_ID)
public class DEUF
{
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final String MOD_ID = "deuf";
    
    public DEUF()
    {
        MinecraftForge.EVENT_BUS.addListener(this::fix);
    }
    
    public void fix(ChunkEvent.Load event)
    {
        if(!(event.getChunk() instanceof Chunk))
        {
            return;
        }
        
        Chunk chunk = (Chunk) event.getChunk();
        if(chunk.getWorldForge() != null && !chunk.getWorldForge().isClientSide && chunk.getWorldForge() instanceof ServerWorld)
        {
            ServerWorld world = (ServerWorld) chunk.getWorldForge();
            ClassInheritanceMultiMap<Entity>[] entities = chunk.getEntitySections();
            
            for(ClassInheritanceMultiMap<Entity> classinheritancemultimap : entities)
            {
                Collection<Entity> entityCollection = com.google.common.collect.ImmutableList.copyOf(classinheritancemultimap);
                
                for(Entity entity : entityCollection)
                {
                    if(!(entity instanceof PlayerEntity))
                    {
                        UUID uuid = entity.getUUID();
                        
                        if(world.getEntity(uuid) != entity)
                        {
                            UUID uuidNew = MathHelper.createInsecureUUID(new Random());
                            while(world.getEntity(uuidNew) != null)
                            {
                                uuidNew = MathHelper.createInsecureUUID(new Random());
                            }
                            
                            entity.setUUID(uuidNew);
                            
                            DEUF.LOGGER.info("Changing UUID of entity {} that already existed from {} to {}", entity.getType().getRegistryName().toString(), uuid.toString(), uuidNew.toString());
                        }
                    }
                }
            }
        }
    }
}