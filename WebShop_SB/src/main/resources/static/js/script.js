document.addEventListener("DOMContentLoaded", function() {
	let currentIndex = 0; // スライダーの現在のインデックス
	let isDragging = false; // ドラッグ状態を管理
	let autoSlideInterval; // 自動スライドのインターバル
	let autoSlideTimeout; // 自動スライドのタイムアウト
	const IMAGE_WIDTH = 150; // 画像の幅（適切な値に設定）
	const wrappers = document.querySelectorAll('.slider-image-wrapper'); // スライダーのラッパー要素
	const total = wrappers.length; // スライダーの総数
	const searchForm = document.getElementById('search-form');
	const searchInput = document.getElementById('search-input');
	const searchButton = document.getElementById('search-btn');
	const recordButton = document.getElementById('start-record-btn');
	const track = document.getElementById("sliderTrack");
	
	let isRecording = false;
	let isAnalyzing = false;
	let mediaRecorder = null;
	let audioChunks = [];
	
	//録音ボタン
	if (recordButton) {
			recordButton.addEventListener('click', async () => {
				const buttonIcon = document.getElementById('button-icon');
				const searchPadding = document.querySelector('.serch-padding');

				if (!isRecording && !isAnalyzing) {
					isRecording = true;
					recordButton.classList.add('recording');
					recordButton.style.backgroundColor = 'red';
					buttonIcon.innerHTML = getRecordingIcon();
					searchPadding.innerText = '録音中';

					try {
						const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
						audioChunks = [];
						mediaRecorder = new MediaRecorder(stream);

						mediaRecorder.ondataavailable = (event) => {
							audioChunks.push(event.data);
						};

						mediaRecorder.onstop = async () => {
							showAnalyzingState();
							const audioBlob = new Blob(audioChunks, { type: 'audio/mp3' });
							const transcript = await sendAudioToApi(audioBlob);
							document.getElementById('search-input').value = transcript;
							resetButton();
						};

						mediaRecorder.start();
					} catch (error) {
						console.error('録音開始中にエラー:', error);
						await handleRecordingError();
						resetButton();
						isRecording = false;
					}
				} else if(isRecording && !isAnalyzing){
					isRecording = false;
					if (mediaRecorder && mediaRecorder.state === 'recording') {
						mediaRecorder.stop();
					}
				}
			});
		}




	// 検索ボタンの設定
	if (searchButton) {
		searchButton.addEventListener('click', function() {
			const searchValue = searchInput.value;
			if (searchValue.trim() !== '') {
				searchForm.submit(); // 検索フォームを送信
			}
		});
	} else {
		console.error("searchButtonが見つかりません。");
	}

	async function handleRecordingError() {
		const existingAudioPath = 'wav/denoised.mp3'; // 既存のWAVファイルのパス
		try {
			const response = await fetch(existingAudioPath);
			if (!response.ok) throw new Error('WAVファイルの読み込みに失敗しました');
			const audioBlob = await response.blob();
			const transcript = await sendAudioToApi(audioBlob);
			console.log('エラー時の文字列:', transcript); // コンソールに出力
			document.getElementById('search-input').value = transcript; // テキストを検索欄に入力
		} catch (error) {
			console.error('エラー処理中にエラーが発生しました:', error);
		}
	}


	async function sendAudioToApi(audioBlob) {
		const formData = new FormData();
		    formData.append('audioFile', audioBlob, 'recording.mp3');

		    const response = await fetch('/api/speech/recognize', {
		        method: 'POST',
		        body: formData
		    });

		    const resultJson = await response.json();
		    document.getElementById('search-input').value = resultJson.text;
			
			return resultJson.text;
	}


	function resetButton() {
		recordButton.classList.remove('recording');
		recordButton.style.backgroundColor = ''; // ボタンの色を元に戻す
		const buttonIcon = document.getElementById('button-icon');
		buttonIcon.innerHTML = getInitialIcon(); // 初期アイコンに戻す
		const searchPadding = document.querySelector('.serch-padding'); // 音声入力のテキストを取得
		searchPadding.innerText = '音声入力'; // テキストを元に戻す
		isAnalyzing = false;
	}

	function getInitialIcon() {
		return `
        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#1f1f1f">
            <path d="M480-400q-50 0-85-35t-35-85v-240q0-50 35-85t85-35q50 0 85 35t35 85v240q0 50-35 85t-85 35Zm0-240Zm-40 520v-123q-104-14-172-93t-68-184h80q0 83 58.5 141.5T480-320q83 0 141.5-58.5T680-520h80q0 105-68 184t-172 93v123h-80Zm40-360q17 0 28.5-11.5T520-520v-240q0-17-11.5-28.5T480-800q-17 0-28.5 11.5T440-760v240q0 17 11.5 28.5T480-480Z"/>
        </svg>
    `;
	}

	function getRecordingIcon() {
		return `
        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#1f1f1f">
            <path d="M80-80v-80q46 0 91-6t88-22q-46-23-72.5-66.5T160-349v-91h160v-120h135L324-822l72-36 131 262q20 40-3 78t-68 38h-56v40q0 33-23.5 56.5T320-360h-80v11q0 35 21.5 61.5T316-252l12 3q40 10 45 50t-31 60q-60 33-126.5 46T80-80Zm572-114-57-56q21-21 33-48.5t12-59.5q0-32-12-59.5T595-466l57-57q32 32 50 74.5t18 90.5q0 48-18 90t-50 74ZM765-80l-57-57q43-43 67.5-99.5T800-358q0-66-24.5-122T708-579l57-57q54 54 84.5 125T880-358q0 81-30.5 152.5T765-80Z"/>
        </svg>
    `;
	}
	async function handleRecordingStop(audioChunks) {
		showAnalyzingState();
		const audioBlob = new Blob(audioChunks, { type: 'audio/mp3' });
		const transcript = await sendAudioToApi(audioBlob);
		console.log('録音からの文字列:', transcript); // コンソールに出力
		document.getElementById('search-input').value = transcript; // テキストを検索欄に入力
	}
	
	function showAnalyzingState() {
		isAnalyzing = true;
		const buttonIcon = document.getElementById('button-icon');
		const searchPadding = document.querySelector('.serch-padding');

		recordButton.style.backgroundColor = '#007bff'; // 青色に変更
		buttonIcon.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#1f1f1f"><path d="M722-322q-56-53-89-125t-33-153q0-81 33-153t89-125l62 64q-44 41-69 96t-25 118q0 63 25 119t69 97l-62 62Zm128-128q-32-29-50-67.5T782-600q0-44 18-82.5t50-67.5l64 64q-18 17-29 38.5T874-600q0 26 11 47.5t29 38.5l-64 64Zm-490 10q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM40-120v-112q0-33 17-62t47-44q51-26 115-44t141-18q77 0 141 18t115 44q30 15 47 44t17 62v112H40Zm80-80h480v-32q0-11-5.5-20T580-266q-36-18-92.5-36T360-320q-71 0-127.5 18T140-266q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T440-600q0-33-23.5-56.5T360-680q-33 0-56.5 23.5T280-600q0 33 23.5 56.5T360-520Zm0-80Zm0 400Z"/></svg>`;
		searchPadding.innerText = '解析中...'; // テキスト変更
	}

	// 自動スライド制御
	function startAutoSlide() {
		stopAutoSlide(); // 念のため重複防止
		autoSlideInterval = setInterval(() => {
			currentIndex = (currentIndex < total - 3) ? currentIndex + 1 : 0;
			updateSlider();
		}, 4000);
	}

	function stopAutoSlide() {
		clearInterval(autoSlideInterval);
		clearTimeout(autoSlideTimeout);
	}

	// 表示更新
	function updateSlider() {
		track.style.transition = 'transform 1s ease';
		track.style.transform = `translateX(-${currentIndex * IMAGE_WIDTH}px)`;

		wrappers.forEach(wrapper => {
			wrapper.classList.remove("visible", "side");
			wrapper.style.opacity = "0"; // 非表示初期化
		});

		for (let i = -1; i <= 3; i++) {
			const index = (currentIndex + i + total) % total;
			const wrapper = wrappers[index];

			if (i === -1 && currentIndex > 0) {
				wrapper.classList.add("side");
				wrapper.style.opacity = "0.5";
			} else if (i >= 0 && i <= 2) {
				wrapper.classList.add("visible");
				wrapper.style.opacity = "1";
			} else if (i === 3 && currentIndex + 3 < total) {
				wrapper.classList.add("side");
				wrapper.style.opacity = "0.5";
			}
		}
	}

	// ドラッグ操作
	track.addEventListener("mousedown", (e) => {
		isDragging = true;
		startX = e.clientX;
		track.style.transition = 'none';
		stopAutoSlide();
	});

	document.addEventListener("mouseup", (e) => {
		if (!isDragging) return;
		isDragging = false;
		const deltaX = e.clientX - startX;

		if (deltaX > 50 && currentIndex > 0) {
			currentIndex--;
		} else if (deltaX < -50 && currentIndex < total - 3) {
			currentIndex++;
		}

		updateSlider();
		autoSlideTimeout = setTimeout(() => {
			startAutoSlide();
		}, 1000);
	});

	track.addEventListener("mouseleave", () => {
		if (isDragging) {
			isDragging = false;
			updateSlider();
			autoSlideTimeout = setTimeout(() => {
				startAutoSlide();
			}, 1000);
		}
	});

	// ハンバーガーメニューのトグル
	const menu = document.querySelector('#header-menu');
	const btn = document.querySelector('#hamburger');

	if (btn) {
		btn.addEventListener('click', () => {
			btn.classList.toggle('open');
			menu.classList.toggle('open');
			menu.style.height = menu.classList.contains("open") ? menu.scrollHeight + 'px' : "0";
		});
	}

	// 位置情報機能
	const locationButton = document.getElementById('get-location');
	if (locationButton) {
		locationButton.addEventListener('click', function() {
			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(
					function(position) {
						document.getElementById('location-output').innerHTML = '緯度：' + position.coords.latitude + '<br>経度：' + position.coords.longitude;
					},
					function(error) {
						document.getElementById('location-output').innerHTML = 'エラー：' + error.message;
					}, {
					enableHighAccuracy: true,
					timeout: 5000,
					maximumAge: 0
				});
			} else {
				document.getElementById('location-output').innerHTML = 'Geolocationはこのブラウザでサポートされていません。';
			}
		});
	}

	// 初期化
	updateSlider();
	startAutoSlide();
});
