package de.cas_ual_ty.deuf;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;

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
        
        Chunk chunk = (Chunk)event.getChunk();
        if(!chunk.getWorldForge().isRemote() && chunk.getWorldForge() instanceof ServerWorld)
        {
            ServerWorld world = (ServerWorld)chunk.getWorldForge();
            ClassInheritanceMultiMap<Entity>[] entities = chunk.getEntityLists();
            UUID uuid;
            UUID uuidNew;
            Collection<Entity> entityCollection;
            for(ClassInheritanceMultiMap<Entity> classinheritancemultimap : entities)
            {
                entityCollection = com.google.common.collect.ImmutableList.copyOf(classinheritancemultimap);
                for(Entity entity : entityCollection)
                {
                    if(!(entity instanceof PlayerEntity))
                    {
                        uuid = entity.getUniqueID();
                        if(world.getEntityByUuid(uuid) != null && world.getEntityByUuid(uuid) != entity)
                        {
                            uuidNew = MathHelper.getRandomUUID(new Random());
                            while(world.getEntityByUuid(uuidNew) != null)
                            {
                                uuidNew = MathHelper.getRandomUUID(new Random());
                            }
                            entity.setUniqueId(uuidNew);
                            DEUF.LOGGER.info("Changing UUID of entity {} that already existed from {} to {}", entity.getType().getRegistryName().toString(), uuid.toString(), uuidNew.toString());
                        }
                    }
                }
            }
        }
    }
}