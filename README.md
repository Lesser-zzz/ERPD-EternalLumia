# ERPD : Eternal Lumia 

> **이터널 리턴의 실험체들이 로그라이크 던전에 갇혔습니다!**

ERPD(Eternal Return Pixel Dungeon)는 유명 로그라이크 게임인 [Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon) 엔진을 기반으로, 이터널 리턴의 캐릭터와 전투/제작 시스템을 픽셀 아트 스타일로 이식하고 있는 오픈소스 팬게임입니다.

---

## 🚀 현재 버전: v0.0.1 (프로토타입)

현재 극초기 프로토타입 단계이며, 게임의 뼈대와 기본 메커니즘을 깎아나가는 중입니다. 

**[ v0.0.1 주요 구현 내용 ]**
* **첫 번째 실험체 '현우' 추가** (기존 전사 클래스 대체)
* **패시브 '도그파이트' 메커니즘 구현**
  * 타격 및 피격 시 스택 증가 (최대 10스택)
  * 10스택 도달 시 Ready 상태 전환
  * 다음 타격 시 추가 피해(5) 및 체력 회복 발동
* (※ 테스트를 위해 개발자 모드로 빌드되어 모든 클래스가 임시 해금되어 있습니다. 현우를 선택해 플레이해 주세요!)

---

## 📥 다운로드 및 실행 방법

최신 빌드는 언제나 [Releases](https://github.com/Lesser-zzz/ERPD-EternalLumia/releases) 페이지에서 다운로드할 수 있습니다.

* **💻 Windows (PC):** `ERPD-desktop.zip` 파일을 다운로드하고 압축을 푼 뒤, 폴더 내의 `ERPD.exe`를 실행합니다. (Java 설치 불필요)
* **📱 Android (모바일):** `ERPD-android-debug.apk` 파일을 다운로드하여 스마트폰에 설치합니다. (출처를 알 수 없는 앱 설치 허용 필요)

---

## 👨‍💻 개발자 노트 & 피드백

**"군대에서 깎아 만드는 1인 개발 프로젝트"**
현재 군 복무 중으로, 개인 정비 시간과 싸지방의 한정된 환경을 쪼개어 개발하고 있습니다. 그래픽이나 시스템이 아직 많이 어설프지만, 핵심적인 '재미'와 '현우다움'을 구현하는 것을 1차 목표로 삼고 있습니다.

게임을 플레이해 보시고 생기는 **버그 제보, 밸런스 의견, 기획 아이디어**는 언제든 환영합니다!
* [GitHub Issues](https://github.com/Lesser-zzz/ERPD-EternalLumia/issues)에 자유롭게 남겨주세요.
* 커뮤니티 댓글 피드백도 꼼꼼히 확인하고 있습니다.

---

## 📜 라이선스 및 크레딧 (License & Credits)

* 이 게임은 님블뉴런(Nimble Neuron)의 **'이터널 리턴(Eternal Return)'** IP를 활용한 비공식 팬게임이며, 모든 원작 캐릭터 및 세계관의 저작권은 님블뉴런에 있습니다.
* 이 프로젝트는 Evan Rowland의 **[Shattered Pixel Dungeon](https://github.com/00-Evan/shattered-pixel-dungeon)** 소스 코드를 수정하여 제작되었으며, 원작과 동일하게 **GPL v3 라이선스**를 따릅니다.
