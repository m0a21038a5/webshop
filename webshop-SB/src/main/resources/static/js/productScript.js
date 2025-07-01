document.addEventListener("DOMContentLoaded", function() {
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');
    const searchButton = document.getElementById('search-btn');
    const recordButton = document.getElementById('start-record-btn');


    // 録音ボタンの設定
	if (recordButton) {
		recordButton.addEventListener('click', async () => {
			document.getElementById('search-input').value = ""; // 検索入力をクリア
			if (recordButton.classList.contains('recording')) return; // 録音中は何もしない
			startRecording(); // 録音を開始
		});
	} else {
		console.error("recordButtonが見つかりません。");
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


	// 録音機能の実装
	async function startRecording() { // startRecordingをasyncに変更
		const buttonIcon = document.getElementById('button-icon');
		const searchPadding = document.querySelector('.serch-padding'); // 音声入力のテキストを取得

		recordButton.classList.add('recording');
		recordButton.style.backgroundColor = 'red'; // ボタンの色を赤に変更
		buttonIcon.innerHTML = getRecordingIcon(); // 録音中のアイコンに変更
		searchPadding.innerText = '録音中'; // テキストを「録音中」に変更

		try {
			const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
			const mediaRecorder = new MediaRecorder(stream);
			const audioChunks = [];
			mediaRecorder.start();
			setTimeout(() => mediaRecorder.stop(), 4000); // 4秒後に録音を停止
			mediaRecorder.ondataavailable = event => audioChunks.push(event.data);
			mediaRecorder.onstop = async () => {
				await handleRecordingStop(audioChunks);
				resetButton(); // 録音が終了した後にボタンをリセット
			};
		} catch (error) {
			console.error('録音中にエラーが発生しました:', error);
			await handleRecordingError(); // エラー処理を呼び出す
			resetButton();
		}
	}

	async function handleRecordingError() {
		const existingAudioPath = 'wav/Search_NatumeSouseki.wav'; // 既存のWAVファイルのパス
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
		formData.append('audioFile', audioBlob, 'recording.wav');

		try {
			const response = await fetch('/api/speech/recognize', {
				method: 'POST',
				body: formData
			});

			if (!response.ok) {
				const errorText = await response.text(); // エラーメッセージを取得
				console.error('音声認識に失敗しました:', errorText);
				throw new Error('音声認識に失敗しました');
			}

			return await response.text(); // 文字列を返す
		} catch (error) {
			console.error('API呼び出し中にエラーが発生しました:', error);
		}
	}


	function resetButton() {
		recordButton.classList.remove('recording');
		recordButton.style.backgroundColor = ''; // ボタンの色を元に戻す
		const buttonIcon = document.getElementById('button-icon');
		buttonIcon.innerHTML = getInitialIcon(); // 初期アイコンに戻す
		const searchPadding = document.querySelector('.serch-padding'); // 音声入力のテキストを取得
		searchPadding.innerText = '音声入力'; // テキストを元に戻す
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

    async function handleRecordingError() {
        const existingAudioPath = '/wav/Search_NatumeSouseki.wav'; // 既存のWAVファイルのパス
        const response = await fetch(existingAudioPath);
        const audioBlob = await response.blob();
        const transcript = await sendAudioToApi(audioBlob);
        document.getElementById('search-input').value = transcript;
    }

    async function sendAudioToApi(audioBlob) {
        const formData = new FormData();
        formData.append('audioFile', audioBlob, 'recording.wav');

        const response = await fetch('/api/speech/recognize', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error('音声認識に失敗しました');
        }

        return await response.text(); // 文字列を返す
    }
});
