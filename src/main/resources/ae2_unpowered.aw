accessWidener	v1	named

# GUI rendering
extendable method net/minecraft/client/gui/screens/inventory/AbstractContainerScreen renderSlot (Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/inventory/Slot;)V
extendable method net/minecraft/client/gui/components/EditBox renderHighlight (IIII)V
accessible method net/minecraft/client/gui/components/EditBox getMaxLength ()I
accessible method net/minecraft/client/gui/components/EditBox isEditable ()Z
accessible field net/minecraft/client/gui/components/AbstractWidget height I
extendable method net/minecraft/client/gui/screens/inventory/AbstractContainerScreen isHovering (Lnet/minecraft/world/inventory/Slot;DD)Z
accessible method net/minecraft/client/gui/screens/inventory/AbstractContainerScreen findSlot (DD)Lnet/minecraft/world/inventory/Slot;

# We need to change yPos of existing slots to resize the container
mutable field net/minecraft/world/inventory/Slot x I
mutable field net/minecraft/world/inventory/Slot y I

# For JEI registration
accessible method net/minecraft/world/item/crafting/RecipeManager byType (Lnet/minecraft/world/item/crafting/RecipeType;)Ljava/util/Map;

# To disable water-bobbing of item entities (for growing crystals)
extendable method net/minecraft/world/entity/item/ItemEntity setUnderwaterMovement ()V

# For structure registration
accessible field net/minecraft/world/level/levelgen/feature/StructureFeature STEP Ljava/util/Map;
accessible method net/minecraft/data/worldgen/StructureFeatures register (Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/levelgen/feature/ConfiguredStructureFeature;)Lnet/minecraft/core/Holder;
accessible method net/minecraft/world/level/levelgen/structure/pieces/StructurePieceType setPieceId (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceType$ContextlessType;Ljava/lang/String;)Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceType;

# features
accessible method net/minecraft/data/worldgen/placement/OrePlacements commonOrePlacement (ILnet/minecraft/world/level/levelgen/placement/PlacementModifier;)Ljava/util/List;

# For overlay rendering
accessible class net/minecraft/client/renderer/RenderStateShard$LineStateShard

# Rendering voxel shape outlines
accessible method net/minecraft/client/renderer/LevelRenderer renderShape (Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDFFFF)V

# For Encoded Pattern Rendering
accessible method net/minecraft/client/renderer/entity/ItemRenderer renderGuiItem (Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V

# Fabric's Attack Block Hook doesn't set the interaction delay
accessible field net/minecraft/client/multiplayer/MultiPlayerGameMode destroyDelay I

# Stairs
accessible method net/minecraft/world/level/block/StairBlock <init> (Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V

# BE registration
accessible class net/minecraft/world/level/block/entity/BlockEntityType$BlockEntitySupplier

accessible class net/minecraft/client/renderer/RenderStateShard$TransparencyStateShard

accessible field net/minecraft/client/gui/screens/Screen renderables Ljava/util/List;
accessible field net/minecraft/world/inventory/Slot slot I

accessible field net/minecraft/client/Minecraft itemColors Lnet/minecraft/client/color/item/ItemColors;

accessible field net/minecraft/client/resources/model/ModelManager bakedRegistry Ljava/util/Map;
accessible method net/minecraft/client/renderer/block/model/ItemOverrides <init> ()V

accessible field net/minecraft/client/renderer/block/model/BlockModel FACE_BAKERY Lnet/minecraft/client/renderer/block/model/FaceBakery;
accessible method net/minecraft/client/renderer/block/model/FaceBakery recalculateWinding ([ILnet/minecraft/core/Direction;)V

accessible method net/minecraft/client/renderer/RenderType create (Ljava/lang/String;Lcom/mojang/blaze3d/vertex/VertexFormat;Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;IZZLnet/minecraft/client/renderer/RenderType$CompositeState;)Lnet/minecraft/client/renderer/RenderType$CompositeRenderType;

accessible class net/minecraft/client/renderer/RenderType$CompositeState
accessible field net/minecraft/client/renderer/RenderStateShard POSITION_COLOR_SHADER Lnet/minecraft/client/renderer/RenderStateShard$ShaderStateShard;

extendable method net/minecraft/world/entity/item/PrimedTnt explode ()V

accessible method net/minecraft/advancements/CriteriaTriggers register (Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;

accessible field net/minecraft/world/entity/item/ItemEntity age I

accessible field net/minecraft/world/item/Item craftingRemainingItem Lnet/minecraft/world/item/Item;
mutable field net/minecraft/world/item/Item craftingRemainingItem Lnet/minecraft/world/item/Item;

accessible class net/minecraft/client/renderer/block/model/BlockElementFace$Deserializer

## Tests
accessible field net/minecraft/gametest/framework/GameTestHelper testInfo Lnet/minecraft/gametest/framework/GameTestInfo;

### Datagens
accessible class net/minecraft/client/renderer/block/model/ItemTransform$Deserializer
accessible field net/minecraft/client/renderer/block/model/ItemTransform$Deserializer DEFAULT_ROTATION Lcom/mojang/math/Vector3f;
accessible field net/minecraft/client/renderer/block/model/ItemTransform$Deserializer DEFAULT_TRANSLATION Lcom/mojang/math/Vector3f;
accessible field net/minecraft/client/renderer/block/model/ItemTransform$Deserializer DEFAULT_SCALE Lcom/mojang/math/Vector3f;
accessible field net/minecraft/client/renderer/block/model/BlockModel$GuiLight name Ljava/lang/String;
accessible field net/minecraft/client/resources/ClientPackSource BUILT_IN Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;
accessible method net/minecraft/client/renderer/block/model/BlockElement uvsByFace (Lnet/minecraft/core/Direction;)[F
accessible field net/minecraft/data/loot/BlockLoot HAS_NO_SILK_TOUCH Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
