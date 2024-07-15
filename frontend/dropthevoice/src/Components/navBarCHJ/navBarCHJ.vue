<template>
    <nav class="bottom-nav">
      <a href="#home" class="nav-item active">
        <img src="@/assets/home-icon.png" alt="Home Icon" />
        <span>Home</span>
      </a>
      <a href="#browse" class="nav-item">
        <img src="@/assets/video-icon.png" alt="Video Icon" />
        <span>Video</span>
      </a>
      <a href="#bookmark" class="nav-item">
        <img src="@/assets/compass-icon.png" alt="Compass Icon" />
        <span>Compass</span>
      </a>
      <a href="#more" class="nav-item">
        <img src="@/assets/more-icon.png" alt="More Icon" />
        <span>More</span>
      </a>
    </nav>
</template>

<style scoped>
.bottom-nav {
  display: flex;
  justify-content: space-around;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: #2e2e2e;
  padding: 10px 0;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #999;
  text-decoration: none;
  font-size: 14px;
}

.nav-item img {
  width: 24px;
  height: 24px;
  margin-bottom: 5px;
}

.nav-item.active {
  color: #bb86fc;
}
</style>